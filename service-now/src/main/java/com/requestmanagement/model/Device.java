package com.requestmanagement.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hostname;

    @Column(nullable = false)
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SoftwareRequest> requests;

    // Constructors
    public Device() {}

    public Device(String hostname, String ipAddress, User assignedUser) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.assignedUser = assignedUser;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }

    public List<SoftwareRequest> getRequests() { return requests; }
    public void setRequests(List<SoftwareRequest> requests) { this.requests = requests; }
}