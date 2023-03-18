package org.experimentV1.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Experiment {

    // A user can provide his own list of words
    // Or request a list of randomized words given the number of words
    // Or request to retrieve from the database with the following parameters:
        // 1. Number of words 2.frequency range(between A and B range || less than X || more than X)
        // 3. Length of words(fixed: only 3 or 4 or 5 or 6 || mixed: include preferred length)
        // 4. Number of seconds to show a word: for storing purposes(handled by the front)
    @Id
    @GeneratedValue()
    private Long experimentId;

    @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters.")
    @NotNull(message = "Please provide a name")
    private String name;

    @Size(max = 500, message = "The description can't be longer than 500 characters.")
    @NotNull(message = "Please, provide a description")
    private String description;
    private final String  createdBy = "USER";
    @NotNull(message = "Please, provide a number of words")
    private Integer numberOfWords;
    //@NotNull(message = "Please, provide a LengthOfWords")
    @Type(ListArrayType.class)
    @Column(
            columnDefinition = "integer[]"
    )
    private List<Integer> lengthOfWords;// 3, 4, 5, 6

    @Type(ListArrayType.class)
    @Column(
            columnDefinition = "integer[]"
    )
    private List<Integer> frequencyRange;

    @NotNull(message = "Please, provide a NumberOfSecondsPerWord")
    private Double numberOfSecondsPerWord;

    @Type(ListArrayType.class)
    @Column(
            columnDefinition = "text[]"
    )
    private List<String> words;

}
