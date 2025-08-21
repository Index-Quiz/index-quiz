package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAnswerPersistenceAdapter implements SaveUserAnswerPort {

    private final UserAnswerJpaRepository userAnswerJpaRepository;

    private final UserAnswerMapper userAnswerMapper;

    @Override
    public void saveUserAnswers(List<UserAnswer> userAnswers) {
        List<UserAnswerEntity> userAnswerEntities = userAnswers.stream()
                .map(userAnswerMapper::mapToUserAnswerEntity)
                .toList();
        userAnswerJpaRepository.saveAll(userAnswerEntities);
    }
}
