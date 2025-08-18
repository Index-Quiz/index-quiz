package com.example.indexquiz.solution.adapter.out.persistence;

import com.example.indexquiz.answer.adapter.out.persistence.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
