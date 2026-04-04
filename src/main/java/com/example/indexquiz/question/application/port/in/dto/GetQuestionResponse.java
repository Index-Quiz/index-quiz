package com.example.indexquiz.question.application.port.in.dto;

import com.example.indexquiz.question.domain.QuestionType;
import java.util.List;

public record GetQuestionResponse(
        long id,
        QuestionType type,
        String content,
        List<GetQuestionOptionResponse> options
) {

}
