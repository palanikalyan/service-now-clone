package com.requestmanagement.dto;

import java.time.LocalDateTime;

public class SoftwareRequestDto {
    private Long id;
    private String requestedByUsername;
    private String deviceHostname;
    private String softwareName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public SoftwareRequestDto() {}

    public SoftwareRequestDto(Long id, String requestedByUsername, String deviceHostname,
                              String softwareName, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requestedByUsername = requestedByUsername;
        this.deviceHostname = deviceHostname;
        this.softwareName = softwareName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequestedByUsername() { return requestedByUsername; }
    public void setRequestedByUsername(String requestedByUsername) { this.requestedByUsername = requestedByUsername; }

    public String getDeviceHostname() { return deviceHostname; }
    public void setDeviceHostname(String deviceHostname) { this.deviceHostname = deviceHostname; }

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}