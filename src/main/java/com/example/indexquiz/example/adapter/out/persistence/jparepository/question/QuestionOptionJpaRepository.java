package com.example.indexquiz.example.adapter.out.persistence.jparepository.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionOptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionJpaRepository extends JpaRepository<QuestionOptionEntity, Long> {

    // TODO: change to package-private when change package structure.

    List<QuestionOptionEntity> findAllByQuestionId(long questionId);
}
