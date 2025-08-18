package com.example.indexquiz.question.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionJpaRepository extends JpaRepository<QuestionOptionEntity, Long> {

    // TODO: change to package-private when change package structure.

    List<QuestionOptionEntity> findAllByQuestionId(long questionId);
}
