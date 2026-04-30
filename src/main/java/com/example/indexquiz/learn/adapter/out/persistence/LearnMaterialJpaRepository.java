package com.example.indexquiz.learn.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface LearnMaterialJpaRepository extends JpaRepository<LearnMaterialEntity, Long> {

    List<LearnMaterialEntity> findAllByQuestionSetOrderByDisplayOrder(QuestionSet questionSet);
}
