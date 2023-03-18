package org.experimentV1.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.experimentV1.entity.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    @Query(value = "select word from all_words order by random() limit :numberOfWords", nativeQuery = true)
    List<String> findRandomWords(Integer numberOfWords);

    @Query(value = """
            select word from nouns_length_3
            where frequency between :lower and :upper
            order by random() limit :numberOfWords""", nativeQuery = true)
    List<String> findWordsByFrequencyRangeAndLength3(Integer numberOfWords, Integer lower, Integer upper);

    @Query(value = """
            select word from nouns_length_4
            where frequency between :lower and :upper
            order by random() limit :numberOfWords""", nativeQuery = true)
    List<String> findWordsByFrequencyRangeAndLength4(Integer numberOfWords, Integer lower, Integer upper);

    @Query(value = """
            select word from nouns_length_5
            where frequency between :lower and :upper
            order by random() limit :numberOfWords""", nativeQuery = true)
    List<String> findWordsByFrequencyRangeAndLength5(Integer numberOfWords, Integer lower, Integer upper);

    @Query(value = """
            select word from nouns_length_6
            where frequency between :lower and :upper
            order by random() limit :numberOfWords""", nativeQuery = true)
    List<String> findWordsByFrequencyRangeAndLength6(Integer numberOfWords, Integer lower, Integer upper);


    @Query(value = """
            select word from all_words
            where frequency between :lower and :upper
            order by random() limit :numberOfWords"""
            , nativeQuery = true)
    List<String> findWordsByFrequency(Integer numberOfWords, Integer lower, Integer upper);

//    """
//            select word from nouns_length_:tableName
//            order by random() limit :numberOfWords"""

    // Temporary solution. Table names cannot be dynamically inserted
    @Query(value =     """
            select word from nouns_length_5
            order by random() limit :numberOfWords"""
            , nativeQuery = true)
    List<String> findWordsByLength(Integer numberOfWords
                                   // ,Integer length
                                   );



//    """
//            select word from nouns_length_:length
//            where frequency between :lower and :upper
//            order by random() limit :numberOfWords"""
    @Query(value =     """
            select word from nouns_length_5
            where frequency between :lower and :upper
            order by random() limit :numberOfWords"""
            , nativeQuery = true)
    List<String> findWordsByLengthAndFrequency(Integer numberOfWords,
                                               //Integer length,
                                               Integer lower, Integer upper);


}
