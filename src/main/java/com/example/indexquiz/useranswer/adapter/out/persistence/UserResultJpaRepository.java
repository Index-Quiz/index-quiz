package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserResultJpaRepository extends JpaRepository<UserResultEntity, Long> {

    @Query("SELECT ur.score, COUNT(ur) FROM UserResultEntity ur WHERE ur.questionSet = :questionSet GROUP BY ur.score")
    List<Object[]> countByQuestionSetGroupByScore(@Param("questionSet") QuestionSet questionSet);
}
