package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remototech.remototechapi.entities.Report;

public interface ReportRepository extends JpaRepository<Report, UUID> {

}
