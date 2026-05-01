package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorQuestionCompletionJpaRepository extends JpaRepository<VisitorQuestionCompletionEntity, Long> {

    boolean existsByVisitorIdAndQuestionId(String visitorId, long questionId);

    @Query("SELECT COUNT(v) FROM VisitorQuestionCompletionEntity v "
         + "WHERE v.visitorId = :visitorId AND v.questionSet = :questionSet")
    long countByVisitorIdAndQuestionSet(
            @Param("visitorId") String visitorId,
            @Param("questionSet") QuestionSet questionSet);
}
