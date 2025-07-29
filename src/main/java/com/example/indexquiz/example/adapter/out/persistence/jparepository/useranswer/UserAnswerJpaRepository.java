package com.example.indexquiz.example.adapter.out.persistence.jparepository.useranswer;

import com.example.indexquiz.example.adapter.out.persistence.entity.useranswer.UserAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerJpaRepository extends JpaRepository<UserAnswerEntity, Long> {

}
