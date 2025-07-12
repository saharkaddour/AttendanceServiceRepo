package com.education.attendance.service;

import com.education.attendance.attendanceService.DTO.attendanceDTO;
import com.education.attendance.attendanceService.attendance;
import com.education.attendance.repository.attendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest(classes = mainAttendanceService.class)
@ExtendWith(MockitoExtension.class)
class mainAttendanceServiceTest {

        @Mock
        private attendanceRepository attendanceRepository;

        @InjectMocks
        private mainAttendanceService mainAttendanceService; // Your actual service instance

        private attendance sampleEntity;
        private attendanceDTO sampleDTO;
        private LocalDateTime fixedTestTime;
        private LocalDateTime fixedTestExamDate;


        @BeforeEach
        void setUp() {
            // REMOVE: MockitoAnnotations.openMocks(this); -- @ExtendWith handles this

            fixedTestTime = LocalDateTime.of(2025, 6, 26, 10, 0, 0);
            fixedTestExamDate = LocalDateTime.of(2025, 7, 10, 9, 0, 0);

            // Ensure sampleDTO contains all @NotBlank / @NotNull fields
            sampleDTO = new attendanceDTO(
                    "1",
                    "student1",
                    "Alice",
                    "alice@example.com",
                    "course1",
                    "Math",
                    "MTH101",
                    "exam1", // Make sure this is not blank
                    fixedTestExamDate, // Use fixed date
                    "Room A", // Make sure this is not blank
                    fixedTestTime, // Use fixed time
                    true,
                    "COURSE",
                    "QR123"
            );

            // Directly create the entity with an ID if it's expected to be saved with one
            // If ID is truly DB-generated, mock the save to return an entity with a generated ID.
            // For simplicity in tests, often we create the entity with an ID if we expect it later.
            sampleEntity = new attendance(
                    "1",
                    "student1",
                    "Alice",
                    "alice@example.com",
                    "course1",
                    "Math",
                    "MTH101",
                    "exam1",
                    fixedTestExamDate,
                    "Room A",
                    fixedTestTime,
                    true,
                    "COURSE",
                    "QR123"
            );
        }

        // --- Test for markAttendance ---
        @Test
        void testMarkAttendance_shouldSaveAttendanceAndReturnDto() {
            when(attendanceRepository.save(any(attendance.class))).thenReturn(sampleEntity);

            attendanceDTO result = mainAttendanceService.markAttendance(sampleDTO);

            assertNotNull(result);
            assertEquals(sampleDTO.getId(), result.getId()); // Check if ID is correctly set
            assertEquals("student1", result.getStudentId());
            assertEquals("Alice", result.getStudentName());
            assertEquals(sampleDTO.getAttendanceTime(), result.getAttendanceTime()); // Verify time is preserved
            verify(attendanceRepository, times(1)).save(any(attendance.class));
        }

    @Test
    void testMarkAttendance_shouldSetAttendanceTimeIfNull() {
        attendanceDTO dtoWithNullTime = new attendanceDTO(
                "2", "student2", "Bob", "bob@example.com",
                "course2", "Physics", "PHY201",
                "exam2", fixedTestExamDate, "Room B",
                null, // attendanceTime is null here
                false, "EXAM", "QR456"
        );
        // Mock save to return an entity with a non-null attendanceTime
        when(attendanceRepository.save(any(attendance.class))).thenAnswer(invocation -> {
            attendance entityArg = invocation.getArgument(0);
            assertNotNull(entityArg.getAttendanceTime()); // Verify service set it
            return entityArg;
        });

        mainAttendanceService.markAttendance(dtoWithNullTime);

        verify(attendanceRepository, times(1)).save(any(attendance.class));
    }
    @Test
    void testMarkAttendance_shouldSetDefaultTypeIfNull() {
        attendanceDTO dtoWithNullType = new attendanceDTO(
                "3", "student3", "Charlie", "charlie@example.com",
                "course3", "Chemistry", "CHM301",
                "exam3", fixedTestExamDate, "Room C",
                fixedTestTime, true,
                null, // Type is null here
                "QR789"
        );
        // Mock save to return an entity with a non-null type
        when(attendanceRepository.save(any(attendance.class))).thenAnswer(invocation -> {
            attendance entityArg = invocation.getArgument(0);
            assertEquals("COURSE", entityArg.getType()); // Verify service set default type
            return entityArg;
        });

        mainAttendanceService.markAttendance(dtoWithNullType);

        verify(attendanceRepository, times(1)).save(any(attendance.class));
    }


// --- Tests for getAllAttendances ---

    @Test
    void testGetAllAttendances_shouldReturnListOfDtos() {
        attendance anotherEntity = new attendance(
                "2", "student2", "Bob", "bob@example.com",
                "course2", "Physics", "PHY201",
                "exam2", fixedTestExamDate, "Room B",
                fixedTestTime, false, "EXAM", "QR456"
        );
        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(sampleEntity, anotherEntity));

        List<attendanceDTO> result = mainAttendanceService.getAllAttendances();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getStudentName());
        assertEquals("Bob", result.get(1).getStudentName());
        verify(attendanceRepository, times(1)).findAll();
    }
    @Test
    void testGetAllAttendances_shouldReturnEmptyList_whenNoAttendances() {
        when(attendanceRepository.findAll()).thenReturn(Collections.emptyList());

        List<attendanceDTO> result = mainAttendanceService.getAllAttendances();

        assertTrue(result.isEmpty());
        verify(attendanceRepository, times(1)).findAll();
    }


    // --- Tests for getAttendanceById ---
    @Test
    void testGetAttendanceById_Found() {
        when(attendanceRepository.findById("1")).thenReturn(Optional.of(sampleEntity));

        Optional<attendanceDTO> result = mainAttendanceService.getAttendanceById("1");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getStudentName());
        verify(attendanceRepository, times(1)).findById("1");
    }
    @Test
    void testGetAttendanceById_NotFound() {
        when(attendanceRepository.findById("2")).thenReturn(Optional.empty());

        Optional<attendanceDTO> result = mainAttendanceService.getAttendanceById("2");

        assertFalse(result.isPresent());
        verify(attendanceRepository, times(1)).findById("2"); // Verify interaction even if not found
    }


    // --- Tests for deleteAttendance ---
    @Test
    void testDeleteAttendance_Exists() {
        when(attendanceRepository.existsById("1")).thenReturn(true);
        doNothing().when(attendanceRepository).deleteById("1"); // For void methods

        boolean result = mainAttendanceService.deleteAttendance("1");

        assertTrue(result);
        verify(attendanceRepository, times(1)).existsById("1");
        verify(attendanceRepository, times(1)).deleteById("1");
    }
    @Test
    void testDeleteAttendance_NotExists() {
        when(attendanceRepository.existsById("999")).thenReturn(false);

        boolean result = mainAttendanceService.deleteAttendance("999");

        assertFalse(result);
        verify(attendanceRepository, times(1)).existsById("999");
        verify(attendanceRepository, never()).deleteById(anyString()); // Verify deleteById was NOT called
    }



    // --- Tests for updateAttendance ---
    @Test
    void testUpdateAttendance_Found() {
        attendanceDTO updatedDTO = new attendanceDTO(
                "1", "student1", "Alice Updated", "alice.updated@example.com",
                "course1", "Math", "MTH101",
                "exam1", fixedTestExamDate, "Room A",
                fixedTestTime.plusHours(1), // Updated time
                false, // Changed to false
                "EXAM", // Changed type
                "QR456" // Updated QR code
        );

        // When findById is called, return the existing entity
        when(attendanceRepository.findById("1")).thenReturn(Optional.of(sampleEntity));
        // When save is called, return the updated entity (which Mockito captures and uses)
        when(attendanceRepository.save(any(attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        attendanceDTO result = mainAttendanceService.updateAttendance("1", updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.getStudentName(), result.getStudentName());
        assertEquals(updatedDTO.getStudentEmail(), result.getStudentEmail());
        assertEquals(updatedDTO.getAttendanceTime(), result.getAttendanceTime());
        assertEquals(updatedDTO.isPresent(), result.isPresent());
        assertEquals(updatedDTO.getType(), result.getType());
        assertEquals(updatedDTO.getQrCodeUsed(), result.getQrCodeUsed());

        verify(attendanceRepository, times(1)).findById("1");
        verify(attendanceRepository, times(1)).save(any(attendance.class));
    }
    @Test
    void testUpdateAttendance_NotFound() {
        when(attendanceRepository.findById("404")).thenReturn(Optional.empty());

        attendanceDTO result = mainAttendanceService.updateAttendance("404", sampleDTO);

        assertNull(result);
        verify(attendanceRepository, times(1)).findById("404");
        verify(attendanceRepository, never()).save(any(attendance.class));
    }



    // --- Test for getAttendancesByStudentId ---
    @Test
    void testGetAttendancesByStudentId_shouldReturnListOfDtos() {
        attendance student1Attendance1 = new attendance(
                "att1", "student1", "Alice", "alice@example.com",
                "course1", "Math", "MTH101", "exam1", fixedTestExamDate, "Room A",
                fixedTestTime, true, "COURSE", "QR123"
        );
        attendance student1Attendance2 = new attendance(
                "att2", "student1", "Alice", "alice@example.com",
                "course2", "Physics", "PHY201", "exam2", fixedTestExamDate.plusDays(1), "Room B",
                fixedTestTime.plusHours(1), true, "EXAM", "QR456"
        );

        when(attendanceRepository.findByStudentId("student1")).thenReturn(Arrays.asList(student1Attendance1, student1Attendance2));

        List<attendanceDTO> result = mainAttendanceService.getAttendancesByStudentId("student1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("student1", result.get(0).getStudentId());
        assertEquals("student1", result.get(1).getStudentId());
        verify(attendanceRepository, times(1)).findByStudentId("student1");
    }
    @Test
    void testGetAttendancesByStudentId_shouldReturnEmptyList_whenNotFound() {
        when(attendanceRepository.findByStudentId("nonExistentStudent")).thenReturn(Collections.emptyList());

        List<attendanceDTO> result = mainAttendanceService.getAttendancesByStudentId("nonExistentStudent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(attendanceRepository, times(1)).findByStudentId("nonExistentStudent");
    }

    }

