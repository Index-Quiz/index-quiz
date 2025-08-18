package com.example.indexquiz.question.application.port.in;

import com.example.indexquiz.question.adapter.in.web.dto.GetQuestionResponse;

public interface QuestionUseCase {

    GetQuestionResponse getQuestion(long questionId);
}
