package com.seniorproject.first.prototype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // mb will delete
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
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
    private ParticipantStatus status;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "participant_id",
            referencedColumnName = "userId"
    )
    private User participant;

//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
    //@JsonIgnore
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "experiment_id",
            referencedColumnName = "experimentId"
    )
    private Experiment experiment;

    @Type(type = "list-array")
    @Column(
            columnDefinition = "int[]"
    )
    private List<Integer> participantResults;

    //@JsonIgnore
//    public void setExperiment(Experiment experiment) {
//        this.experiment = experiment;
//    }
//
//    //@JsonIgnore
//    public Experiment getExperiment() {
//        return this.experiment;
//    }
}