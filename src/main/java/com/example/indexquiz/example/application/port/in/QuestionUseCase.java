package com.example.indexquiz.example.application.port.in;

import com.example.indexquiz.example.adapter.in.web.dto.GetQuestionResponse;

public interface QuestionUseCase {

    GetQuestionResponse getQuestion(long questionId);
}
