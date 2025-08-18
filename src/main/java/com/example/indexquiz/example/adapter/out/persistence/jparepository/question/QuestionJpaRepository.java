package com.example.indexquiz.example.adapter.out.persistence.jparepository.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    // TODO: change to package-private when change package structure.
}
