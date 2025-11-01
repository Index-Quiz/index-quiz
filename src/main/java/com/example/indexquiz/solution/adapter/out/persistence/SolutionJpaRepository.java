package com.example.indexquiz.solution.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface SolutionJpaRepository extends JpaRepository<SolutionEntity, Long> {

    Optional<SolutionEntity> findByQuestionId(long questionId);
}
