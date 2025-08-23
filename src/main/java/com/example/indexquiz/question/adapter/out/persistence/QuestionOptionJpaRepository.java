package com.example.indexquiz.question.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface QuestionOptionJpaRepository extends JpaRepository<QuestionOptionEntity, Long> {

    List<QuestionOptionEntity> findAllByQuestionId(long questionId);
}
