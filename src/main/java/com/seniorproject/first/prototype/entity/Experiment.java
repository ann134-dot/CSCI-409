package com.seniorproject.first.prototype.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seniorproject.first.prototype.util.ExperimentOverallResultsConverter;
import com.seniorproject.first.prototype.util.ExperimentWordsConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
//@TypeDef(
//        name = "list-array",
//        typeClass = ListArrayType.class
//)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // mb will delete
//@Table(
//        name = "experiments",
//        uniqueConstraints = @UniqueConstraint(
//                name = "email_unique",
//                columnNames =
//        )
//)
public class Experiment {
    @Id
    @SequenceGenerator(
            name = "experimentId_sequence",
            sequenceName = "experimentId_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "experimentId_sequence"
    )
    private Long experimentId;
    private String experimentName;
    private String description;
    private Double betweenWordTime;
    private Double wordTime;
    private Boolean isPublic;

    @SuppressWarnings("JpaAttributeTypeInspection") // bc of ide issue
    @Convert(converter = ExperimentWordsConverter.class)
    private Map<Integer, String> words;

    private Long participantCount;


    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = ExperimentOverallResultsConverter.class)
    private Map<Integer, Integer> overallResults;

    private Long experimentType;

    @JsonBackReference
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "creator_id",
            referencedColumnName = "userId"
    )
    private User creator;

    @OneToMany(
            mappedBy = "experiment",
            fetch = FetchType.EAGER
    )
    private List<Participation> participations;
}
