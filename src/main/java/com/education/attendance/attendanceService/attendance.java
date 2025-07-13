package com.education.attendance.attendanceService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "attendance")
@Data
@AllArgsConstructor // lombok se charge par tt les contructeurs auto
@NoArgsConstructor
public class attendance {

    @Id
    private String id;

    // Étudiant
    private String studentId;
    private String studentName;
    private String studentEmail;

    // Cours
    private String courseId;
    private String courseTitle;
    private String courseCode;

    // Examen (facultatif)
    private String examId;
    private LocalDateTime examDate;
    private String examRoom;

    // Présence
    private LocalDateTime attendanceTime;
    private boolean present;
    private String type;
    private String qrCodeUsed;



}
