package com.example.indexquiz.question.application.port.in.dto;

import com.example.indexquiz.question.domain.QuestionOption;

public record GetQuestionOptionResponse(
        long id,
        String content
) {

    public GetQuestionOptionResponse(QuestionOption questionOption) {
        this(questionOption.getId(), questionOption.getContent());
    }
}
