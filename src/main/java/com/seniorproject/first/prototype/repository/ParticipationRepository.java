package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.ParticipantStatus;
import com.seniorproject.first.prototype.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findParticipationsByExperimentExperimentIdAndStatus(Long experimentId, ParticipantStatus status);
    Participation findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(String userEmail, Long experimentId, ParticipantStatus status);

    List<Participation> findParticipationByExperiment_ExperimentId(Long experimentId);
    Participation findParticipationByParticipantUserEmailAndExperiment_ExperimentId(String userEmail, Long experimentId);

    List<Participation> findParticipationsByParticipantUserEmailAndStatus(String userEmail, ParticipantStatus participantStatus);
}
