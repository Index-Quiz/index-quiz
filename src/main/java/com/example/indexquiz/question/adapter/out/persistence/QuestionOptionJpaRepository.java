package com.example.indexquiz.question.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface QuestionOptionJpaRepository extends JpaRepository<QuestionOptionEntity, Long> {

    List<QuestionOptionEntity> findAllByQuestionId(long questionId);

    @Query("""
    select qoe
    from QuestionOptionEntity qoe
    where qoe.questionId in :questionIds
    """)
    List<QuestionOptionEntity> findAllByQuestionIds(List<Long> questionIds);
}
