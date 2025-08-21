package com.example.indexquiz.question.application.service;

import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.application.port.out.QuestionWithOptionsMapper;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {

    private final GetQuestionPort getQuestionPort;

    private final QuestionWithOptionsMapper questionWithOptionsMapper;

    @Override
    public GetQuestionResponse getQuestion(long questionId) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(questionId);
        return questionWithOptionsMapper.mapToGetQuestionResponse(questionWithOptions);
    }
}
