package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    public List<Experiment> findByCreatorUserIdAndIsPublic(Long userId, Boolean isPublic);
    public Experiment findByExperimentId(Long experimentId);
}
