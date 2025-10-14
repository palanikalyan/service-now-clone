package com.requestmanagement.service;

import com.requestmanagement.dto.CreateRequestDto;
import com.requestmanagement.dto.SoftwareRequestDto;
import com.requestmanagement.exception.ResourceNotFoundException;
import com.requestmanagement.exception.UnauthorizedException;
import com.requestmanagement.model.Device;
import com.requestmanagement.model.SoftwareRequest;
import com.requestmanagement.model.User;
import com.requestmanagement.repository.DeviceRepository;
import com.requestmanagement.repository.SoftwareRequestRepository;
import com.requestmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private SoftwareRequestRepository requestRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    public SoftwareRequestDto createRequest(CreateRequestDto createRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Device device = deviceRepository.findById(createRequestDto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", createRequestDto.getDeviceId()));

        // Check if device is assigned to the requesting user
        if (!device.getAssignedUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only create requests for devices assigned to you");
        }

        SoftwareRequest request = new SoftwareRequest(user, device, createRequestDto.getSoftwareName());
        SoftwareRequest savedRequest = requestRepository.save(request);

        return mapToDto(savedRequest);
    }

    public List<SoftwareRequestDto> getUserRequests() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<SoftwareRequest> requests = requestRepository.findByRequestedById(user.getId());
        return requests.stream().map(this::mapToDto).collect(Collectors.toList());
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