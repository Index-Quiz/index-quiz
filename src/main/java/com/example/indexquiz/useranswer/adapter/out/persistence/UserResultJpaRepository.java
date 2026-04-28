package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.domain.QuestionSetAverage;
import com.example.indexquiz.useranswer.domain.ScoreCount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserResultJpaRepository extends JpaRepository<UserResultEntity, Long> {

    @Query("SELECT new com.example.indexquiz.useranswer.domain.ScoreCount(ur.score, COUNT(ur)) "
         + "FROM UserResultEntity ur WHERE ur.questionSet = :questionSet GROUP BY ur.score")
    List<ScoreCount> countByQuestionSetGroupByScore(@Param("questionSet") QuestionSet questionSet);

    @Query("SELECT new com.example.indexquiz.useranswer.domain.QuestionSetAverage(ur.questionSet, AVG(ur.score)) "
         + "FROM UserResultEntity ur GROUP BY ur.questionSet")
    List<QuestionSetAverage> findAverageScoresByQuestionSet();
}
