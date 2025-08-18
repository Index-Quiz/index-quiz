package com.example.indexquiz.answer.adapter.out.persistence;

import com.example.indexquiz.answer.adapter.out.persistence.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
