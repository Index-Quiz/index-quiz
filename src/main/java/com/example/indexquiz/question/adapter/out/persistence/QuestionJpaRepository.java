package com.example.indexquiz.question.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    // TODO: change to package-private when change package structure.
}
