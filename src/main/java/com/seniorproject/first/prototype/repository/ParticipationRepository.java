package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

}
