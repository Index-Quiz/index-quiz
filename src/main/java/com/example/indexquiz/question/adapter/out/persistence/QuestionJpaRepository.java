package com.example.indexquiz.question.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    Optional<QuestionEntity> findByQuestionOrder(long questionOrder);
}
