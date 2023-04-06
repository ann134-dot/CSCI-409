package com.seniorproject.first.prototype.repository;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    List<Experiment> findByCreatorUserIdAndIsJoinable(Long userId, Boolean isPublic);
    Experiment findByExperimentId(Long experimentId);

    List<Experiment> findByExperimentNameIgnoreCaseAndCreator(String experimentName, User user);
    List<Experiment> findAllByCreator(User user);
    List<Experiment> findAll();

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

}
