package com.seniorproject.first.prototype.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // mb will delete
public class Participation {
    @Id
    @SequenceGenerator(
            name = "participationId_sequence",
            sequenceName = "participationId_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "participationId_sequence"
    )
    private Long participationId;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "participant_id",
            referencedColumnName = "userId"
    )
    private User participant;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "experiment_id",
            referencedColumnName = "experimentId"
    )
    private Experiment experiment;

    private String participantResults;
}