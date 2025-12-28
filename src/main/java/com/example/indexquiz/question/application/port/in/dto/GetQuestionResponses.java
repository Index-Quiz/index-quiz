package com.example.indexquiz.question.application.port.in.dto;

import java.util.List;

public record GetQuestionResponses(
        List<GetQuestionResponse> questions
) {

}
