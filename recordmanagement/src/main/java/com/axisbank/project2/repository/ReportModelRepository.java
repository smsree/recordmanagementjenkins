package com.axisbank.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axisbank.project2.report.model.ReportModel;
@Repository
public interface ReportModelRepository extends JpaRepository<ReportModel, Integer> {
	Optional<ReportModel> findByUsername(String username);
}
