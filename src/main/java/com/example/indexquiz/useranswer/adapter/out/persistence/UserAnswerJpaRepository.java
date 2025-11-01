package com.example.indexquiz.useranswer.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserAnswerJpaRepository extends JpaRepository<UserAnswerEntity, Long> {

    List<UserAnswerEntity> findAllBySubmitId(String submitId);

}
