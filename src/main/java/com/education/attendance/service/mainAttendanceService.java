package com.education.attendance.service;

import com.education.attendance.attendanceService.DTO.attendanceDTO;
import com.education.attendance.attendanceService.attendance;
import com.education.attendance.repository.attendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class mainAttendanceService {
    private final attendanceRepository attendanceRepository;

    @Autowired // Spring will inject the attendanceRepository here
    public mainAttendanceService(attendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    // Create / Mark Attendance
    public attendanceDTO markAttendance(attendanceDTO dto) {
        if (dto.getAttendanceTime() == null) {
            dto.setAttendanceTime(LocalDateTime.now());
        }
        if (dto.getType() == null) {
            dto.setType("COURSE"); // Default type
        }
        attendance entity = dto.toEntity();
        attendance savedEntity = attendanceRepository.save(entity);
        return attendanceDTO.fromEntity(savedEntity);
    }
    // Get All Attendances
    public List<attendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(attendanceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Get Attendance by ID
    public Optional<attendanceDTO> getAttendanceById(String id) {
        return attendanceRepository.findById(id)
                .map(attendanceDTO::fromEntity);
    }
    // Update Attendance
    public attendanceDTO updateAttendance(String id, attendanceDTO dto) {
        Optional<attendance> existingAttendanceOptional = attendanceRepository.findById(id);
        if (existingAttendanceOptional.isPresent()) {
            attendance existingAttendance = existingAttendanceOptional.get();

            // Update fields from DTO
            existingAttendance.setStudentId(dto.getStudentId());
            existingAttendance.setStudentName(dto.getStudentName());
            existingAttendance.setStudentEmail(dto.getStudentEmail());
            existingAttendance.setCourseId(dto.getCourseId());
            existingAttendance.setCourseTitle(dto.getCourseTitle());
            existingAttendance.setCourseCode(dto.getCourseCode());
            existingAttendance.setExamId(dto.getExamId());
            existingAttendance.setExamDate(dto.getExamDate());
            existingAttendance.setExamRoom(dto.getExamRoom());
            existingAttendance.setAttendanceTime(dto.getAttendanceTime() != null ? dto.getAttendanceTime() : existingAttendance.getAttendanceTime());
            existingAttendance.setPresent(dto.isPresent());
            existingAttendance.setType(dto.getType());
            existingAttendance.setQrCodeUsed(dto.getQrCodeUsed());

            attendance updatedEntity = attendanceRepository.save(existingAttendance);
            return attendanceDTO.fromEntity(updatedEntity);
        }
        return null; // Or throw a custom exception
    }

    // Delete Attendance
    public boolean deleteAttendance(String id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // You can add your custom find methods here, using the repository methods
    public List<attendanceDTO> getAttendancesByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(attendanceDTO::fromEntity)
                .collect(Collectors.toList());
    }


    }
