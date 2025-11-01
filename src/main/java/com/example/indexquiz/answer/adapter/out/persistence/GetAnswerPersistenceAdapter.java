package com.example.indexquiz.answer.adapter.out.persistence;

import com.example.indexquiz.answer.adapter.out.mapper.AnswerMapper;
import com.example.indexquiz.answer.application.port.out.GetAnswerPort;
import com.example.indexquiz.answer.domain.Answers;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAnswerPersistenceAdapter implements GetAnswerPort {

    private final AnswerMapper answerMapper;

    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public Answers getByQuestionId(long questionId) {
        return answerJpaRepository.findAllByQuestionId(questionId)
                .stream()
                .map(answerMapper::mapToAnswer)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), Answers::new));
    }
}
