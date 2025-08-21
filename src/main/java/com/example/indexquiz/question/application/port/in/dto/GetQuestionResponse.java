package com.example.indexquiz.question.application.port.in.dto;

import com.example.indexquiz.question.domain.QuestionType;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;

public record GetQuestionResponse(
        long id,
        QuestionType type,
        String content,
        List<GetQuestionOptionResponse> options
) {

    public GetQuestionResponse(QuestionWithOptions questionWithOptions) {
        this(questionWithOptions.getQuestion().getId(),
                questionWithOptions.getQuestion().getType(),
                questionWithOptions.getQuestion().getContent(),
                questionWithOptions.getOptions().stream().map(GetQuestionOptionResponse::new).toList());
    }
}
