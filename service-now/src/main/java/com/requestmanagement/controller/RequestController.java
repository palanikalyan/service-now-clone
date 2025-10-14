package com.requestmanagement.controller;

import com.requestmanagement.dto.CreateRequestDto;
import com.requestmanagement.dto.SoftwareRequestDto;
import com.requestmanagement.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<SoftwareRequestDto> createRequest(@Valid @RequestBody CreateRequestDto createRequestDto) {
        SoftwareRequestDto response = requestService.createRequest(createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SoftwareRequestDto>> getUserRequests() {
        List<SoftwareRequestDto> requests = requestService.getUserRequests();
        return ResponseEntity.ok(requests);
    }
}