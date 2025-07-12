package com.education.attendance.attendanceService.DTO;

import com.education.attendance.attendanceService.attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class attendanceDTO {

    private String id; // Keep ID for updates or responses that include ID

    // Étudiant
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email is not valid"
    )
    private String studentEmail;

    // Cours
    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Course Title is required")
    private String courseTitle;

    @NotBlank(message = "Course Code is required")
    private String courseCode;

    // Examen (facultatif)
    @NotBlank(message = "Exam ID is required")
    private String examId;

    @NotNull(message = "Exam date cannot be null for exam attendance")
    private LocalDateTime examDate;

    private String examRoom;

    // Présence
    @NotNull(message = "Attendance time cannot be null")
    private LocalDateTime attendanceTime;

    @NotNull(message = "Presence status (present) cannot be null")
    private boolean present;

    @NotBlank(message = "Attendance type (COURSE/EXAM) is required")
    private String type;

    private String qrCodeUsed;

    // Convert Entity to DTO
    public static attendanceDTO fromEntity(attendance entity) {
        return new attendanceDTO(
                entity.getId(),
                entity.getStudentId(),
                entity.getStudentName(),
                entity.getStudentEmail(),
                entity.getCourseId(),
                entity.getCourseTitle(),
                entity.getCourseCode(),
                entity.getExamId(),
                entity.getExamDate(),
                entity.getExamRoom(),
                entity.getAttendanceTime(),
                entity.isPresent(),
                entity.getType(),
                entity.getQrCodeUsed()
        );
    }

    // Convert DTO to Entity
    public attendance toEntity() {
        return new attendance(
                this.id,
                this.studentId,
                this.studentName,
                this.studentEmail,
                this.courseId,
                this.courseTitle,
                this.courseCode,
                this.examId,
                this.examDate,
                this.examRoom,
                this.attendanceTime != null ? this.attendanceTime : LocalDateTime.now(),
                this.present,
                this.type,
                this.qrCodeUsed
        );
    }
}
