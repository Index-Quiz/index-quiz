package com.example.indexquiz.example.adapter.in.web.dto;

import com.example.indexquiz.example.domain.question.QuestionType;
import com.example.indexquiz.example.domain.question.QuestionWithOptions;
import java.util.List;

public record GetQuestionResponse(
        long id,
        QuestionType type,
        String content,
        List<String> options
) {

    public GetQuestionResponse(QuestionWithOptions questionWithOptions) {
        this(questionWithOptions.getQuestion().getId(),
                questionWithOptions.getQuestion().getType(),
                questionWithOptions.getQuestion().getContent(),
                questionWithOptions.getOptionContents());
    }
}
