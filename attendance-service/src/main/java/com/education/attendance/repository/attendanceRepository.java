package com.education.attendance.repository;


import com.education.attendance.attendanceService.attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indique à Spring que c'est un composant de persistance
public interface attendanceRepository extends MongoRepository<attendance, String> {
    // Méthodes de recherche personnalisées, si nécessaires
    // Elles seront utilisées pour des requêtes spécifiques que Spring Data peut déduire.
    List<attendance> findByStudentId(String studentId);
    List<attendance> findByCourseId(String courseId);
    List<attendance> findByExamId(String examId);
    List<attendance> findByStudentIdAndCourseId(String studentId, String courseId);
    List<attendance> findByStudentIdAndExamId(String studentId, String examId);
}