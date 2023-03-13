package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    public List<Participation> findParticipationsByExperimentExperimentIdAndStatus(Long experimentId, String status);
    public Participation findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(String userEmail, Long experimentId, String status);

    public Participation findParticipationByParticipantUserEmailAndExperiment_ExperimentId(String userEmail, Long experimentId);

    List<Participation> findParticipationsByParticipantUserEmailAndStatus(String userEmail, String status);
}
