package com.example.indexquiz.question.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

}
