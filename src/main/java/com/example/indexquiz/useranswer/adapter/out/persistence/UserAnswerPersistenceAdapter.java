package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.GetUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAnswerPersistenceAdapter implements SaveUserAnswerPort, GetUserAnswerPort {

    private final UserAnswerJpaRepository userAnswerJpaRepository;

    private final UserAnswerMapper userAnswerMapper;

    @Override
    public UserAnswers saveUserAnswers(SaveUserAnswersCommand command) {
        List<UserAnswerEntity> userAnswers = command.getUserAnswers().stream()
                .map(userAnswerMapper::mapToUserAnswerEntity)
                .toList();
        return userAnswerJpaRepository.saveAll(userAnswers)
                .stream()
                .map(userAnswerMapper::mapToUserAnswer)
                .collect(Collectors.collectingAndThen(Collectors.toList(), UserAnswers::new));
    }

    @Override
    public UserAnswers getBySubmitId(String submitId) {
        return userAnswerJpaRepository.findAllBySubmitId(submitId)
                .stream()
                .map(userAnswerMapper::mapToUserAnswer)
                .collect(Collectors.collectingAndThen(Collectors.toList(), UserAnswers::new));
    }
}
