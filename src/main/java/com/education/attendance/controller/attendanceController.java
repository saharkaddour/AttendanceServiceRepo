package com.education.attendance.controller;


import com.education.attendance.attendanceService.DTO.attendanceDTO;
import com.education.attendance.controller.api.attendanceApi;
import com.education.attendance.service.mainAttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendances")
public class attendanceController implements attendanceApi {
    private final mainAttendanceService mainAttendanceService;

    @Autowired // Inject the AttendanceService
    public attendanceController(mainAttendanceService attendanceService) {
        this.mainAttendanceService = attendanceService;
    }
    @Override
    @PostMapping // Create
    public ResponseEntity<attendanceDTO> markAttendance(@Valid @RequestBody attendanceDTO dto) {
        attendanceDTO savedDto = mainAttendanceService.markAttendance(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @Override
    @GetMapping // Read All
    public ResponseEntity<List<attendanceDTO>> getAll() {
        List<attendanceDTO> attendances = mainAttendanceService.getAllAttendances();
        if (attendances.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content if list is empty
        }
        return ResponseEntity.ok(attendances);
    }


    @Override
    @GetMapping("/{id}") // Read One
    public ResponseEntity<attendanceDTO> getOne(@PathVariable String id) {
        Optional<attendanceDTO> attendanceDto = mainAttendanceService.getAttendanceById(id);
        return attendanceDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PutMapping("/{id}") // Update - ADDED
    public ResponseEntity<attendanceDTO> updateAttendance(@PathVariable String id, @RequestBody attendanceDTO dto) {
        attendanceDTO updatedDto = mainAttendanceService.updateAttendance(id, dto);
        if (updatedDto != null) {
            return ResponseEntity.ok(updatedDto);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping("/{id}") // Delete - ADDED
    public ResponseEntity<HttpStatus> deleteAttendance(@PathVariable String id) {
        boolean deleted = mainAttendanceService.deleteAttendance(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}