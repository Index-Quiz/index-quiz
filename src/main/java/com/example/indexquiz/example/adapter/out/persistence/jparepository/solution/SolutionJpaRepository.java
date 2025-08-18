package com.example.indexquiz.example.adapter.out.persistence.jparepository.solution;

import com.example.indexquiz.example.adapter.out.persistence.entity.answer.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
