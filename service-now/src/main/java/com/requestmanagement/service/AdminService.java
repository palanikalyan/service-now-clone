package com.requestmanagement.service;

import com.requestmanagement.dto.SoftwareRequestDto;
import com.requestmanagement.exception.ResourceNotFoundException;
import com.requestmanagement.model.RequestStatus;
import com.requestmanagement.model.SoftwareRequest;
import com.requestmanagement.repository.SoftwareRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private SoftwareRequestRepository requestRepository;

    @Autowired
    private ShellExecutorService shellExecutorService;

    public List<SoftwareRequestDto> getAllRequests() {
        List<SoftwareRequest> requests = requestRepository.findAllByOrderByCreatedAtDesc();
        return requests.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public SoftwareRequestDto approveRequest(Long requestId) {
        SoftwareRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request", "id", requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }

        request.setStatus(RequestStatus.APPROVED);
        SoftwareRequest savedRequest = requestRepository.save(request);

        // Execute shell script for software installation
        try {
            shellExecutorService.installSoftware(request.getSoftwareName(), request.getDevice().getHostname());
            // Mark as completed after successful installation
            request.setStatus(RequestStatus.COMPLETED);
            savedRequest = requestRepository.save(request);
        } catch (Exception e) {
            // If installation fails, keep status as APPROVED but log the error
            System.err.println("Failed to execute installation script: " + e.getMessage());
        }

        return mapToDto(savedRequest);
    }

    public SoftwareRequestDto rejectRequest(Long requestId) {
        SoftwareRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request", "id", requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }

        request.setStatus(RequestStatus.REJECTED);
        SoftwareRequest savedRequest = requestRepository.save(request);

        return mapToDto(savedRequest);
    }

    private SoftwareRequestDto mapToDto(SoftwareRequest request) {
        return new SoftwareRequestDto(
                request.getId(),
                request.getRequestedBy().getUsername(),
                request.getDevice().getHostname(),
                request.getSoftwareName(),
                request.getStatus().name(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}