package com.seniorproject.first.prototype.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@AllArgsConstructor
@Builder // mb will delete

@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class ExperimentStatistics {
    @Id
    @SequenceGenerator(
            name = "experimentStatisticsId_sequence",
            sequenceName = "experimentStatisticsId_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "experimentStatisticsId_sequence"
    )
    private Long experimentStatisticsId;
    @OneToOne(mappedBy = "experimentStatistics", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private Experiment experiment;

    Double averageAge;
//    Integer numberOfMaleParticipants;
//    Integer numberOfFemaleParticipants;
//    Integer numberOfOtherParticipants;

    // MALE - 0, FEMALE - 1, OTHER - 2
    @Type(type = "list-array")
    @Column(
            columnDefinition = "integer[]"
    )
    List<Integer> numberOfGenderParticipants;

    // Associate - 0, Bachelor - 1, master's - 2, doctoral - 3
    @Type(type = "list-array")
    @Column(
            columnDefinition = "integer[]"
    )
    List<Integer> numberOfDegreeParticipants;

    public ExperimentStatistics(){
        this.experiment = null;
        this.averageAge = (double) 0;
        this.numberOfDegreeParticipants = new ArrayList<Integer>(Collections.nCopies(3, 0));
        this.numberOfGenderParticipants = new ArrayList<Integer>(Collections.nCopies(3, 0));

    }
}
