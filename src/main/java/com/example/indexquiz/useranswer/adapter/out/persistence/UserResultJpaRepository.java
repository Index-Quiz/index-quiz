package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.domain.ScoreCount;
import com.example.indexquiz.useranswer.domain.SetBestScore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserResultJpaRepository extends JpaRepository<UserResultEntity, Long> {

    @Query("SELECT new com.example.indexquiz.useranswer.domain.ScoreCount(ur.score, COUNT(ur)) "
         + "FROM UserResultEntity ur WHERE ur.questionSet = :questionSet GROUP BY ur.score")
    List<ScoreCount> countByQuestionSetGroupByScore(@Param("questionSet") QuestionSet questionSet);

    @Query("SELECT AVG(ur.score) FROM UserResultEntity ur "
         + "WHERE ur.questionSet = :questionSet AND ur.score >= 0 AND ur.score <= :maxScore")
    Optional<Double> findAverageByQuestionSetAndMaxScore(
            @Param("questionSet") QuestionSet questionSet,
            @Param("maxScore") int maxScore);

    @Query("SELECT new com.example.indexquiz.useranswer.domain.SetBestScore(ur.questionSet, MAX(ur.score)) "
            + "FROM UserResultEntity ur "
            + "WHERE ur.visitorId = :visitorId "
            + "AND ur.questionSet not in :excluded "
            + "AND ur.score <= :maxScore "
            + "GROUP BY ur.questionSet")
    List<SetBestScore> findBestScoresByVisitorId(
            @Param("visitorId") String visitorId,
            @Param("maxScore") int maxScore,
            @Param("excluded") List<QuestionSet> excluded
    );
}
