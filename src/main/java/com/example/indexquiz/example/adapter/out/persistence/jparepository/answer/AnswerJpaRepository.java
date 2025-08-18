package com.example.indexquiz.example.adapter.out.persistence.jparepository.answer;

import com.example.indexquiz.example.adapter.out.persistence.entity.answer.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
