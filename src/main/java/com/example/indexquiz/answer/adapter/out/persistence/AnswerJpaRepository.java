package com.example.indexquiz.answer.adapter.out.persistence;

import com.example.indexquiz.answer.domain.Answer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

    List<AnswerEntity> findAllByQuestionId(long questionId);
}
