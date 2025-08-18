package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.useranswer.adapter.out.persistence.UserAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerJpaRepository extends JpaRepository<UserAnswerEntity, Long> {

}
