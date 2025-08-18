package com.example.indexquiz.example.adapter.out.persistence.jparepository.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionJpaRepository extends JpaRepository<QuestionOptionEntity, Long> {

}
