package com.example.indexquiz.example.application.service;

import com.example.indexquiz.example.adapter.in.web.dto.GetQuestionResponse;
import com.example.indexquiz.example.application.port.in.QuestionUseCase;
import com.example.indexquiz.example.application.port.out.GetQuestionPort;
import com.example.indexquiz.example.domain.question.QuestionWithOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {

    private final GetQuestionPort getQuestionPort;

    @Override
    @Transactional(readOnly = true)
    public GetQuestionResponse getQuestion(long questionId) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(questionId);
        return new GetQuestionResponse(questionWithOptions);
    }
}
