package com.education.attendance.controller.api;

import com.education.attendance.attendanceService.DTO.attendanceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/attendance")
public interface attendanceApi {

    // Create Operation (POST)
    @PostMapping
    ResponseEntity<attendanceDTO> markAttendance(@RequestBody attendanceDTO dto);

    // Read All Operation (GET)
    @GetMapping
    ResponseEntity<List<attendanceDTO>> getAll();

    // Read One Operation (GET by ID)
    @GetMapping("/{id}")
    ResponseEntity<attendanceDTO> getOne(@PathVariable String id);

    // Update Operation (PUT) - ADDED
    @PutMapping("/{id}")
    ResponseEntity<attendanceDTO> updateAttendance(@PathVariable String id, @RequestBody attendanceDTO dto);

    // Delete Operation (DELETE) - ADDED
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteAttendance(@PathVariable String id);
}