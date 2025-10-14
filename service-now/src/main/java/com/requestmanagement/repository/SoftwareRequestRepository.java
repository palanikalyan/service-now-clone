package com.requestmanagement.repository;

import com.requestmanagement.model.SoftwareRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoftwareRequestRepository extends JpaRepository<SoftwareRequest, Long> {
    List<SoftwareRequest> findByRequestedById(Long userId);
    List<SoftwareRequest> findAllByOrderByCreatedAtDesc();
}