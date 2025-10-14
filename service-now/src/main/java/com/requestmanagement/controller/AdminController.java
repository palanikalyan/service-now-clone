package com.requestmanagement.controller;

import com.requestmanagement.dto.SoftwareRequestDto;
import com.requestmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/requests")
    public ResponseEntity<List<SoftwareRequestDto>> getAllRequests() {
        List<SoftwareRequestDto> requests = adminService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<SoftwareRequestDto> approveRequest(@PathVariable Long id) {
        SoftwareRequestDto response = adminService.approveRequest(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<SoftwareRequestDto> rejectRequest(@PathVariable Long id) {
        SoftwareRequestDto response = adminService.rejectRequest(id);
        return ResponseEntity.ok(response);
    }
}