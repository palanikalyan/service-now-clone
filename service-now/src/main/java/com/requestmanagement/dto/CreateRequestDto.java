package com.requestmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateRequestDto {
    @NotNull
    private Long deviceId;

    @NotBlank
    private String softwareName;

    // Constructors
    public CreateRequestDto() {}

    public CreateRequestDto(Long deviceId, String softwareName) {
        this.deviceId = deviceId;
        this.softwareName = softwareName;
    }

    // Getters and Setters
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }
}
