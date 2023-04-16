package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.ExperimentStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperimentStatisticsRepository extends JpaRepository<ExperimentStatistics, Long> {
}
