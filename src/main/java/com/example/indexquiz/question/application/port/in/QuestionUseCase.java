package com.example.indexquiz.question.application.port.in;

import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;

public interface QuestionUseCase {

    GetQuestionResponse getQuestion(long questionId);
}
