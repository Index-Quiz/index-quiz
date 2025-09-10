package com.example.indexquiz.useranswer.application.port.in.dto.response;

import java.util.List;

public record GetUserAnswerResponse(
        boolean isCorrect,
        List<Long> userOptions,
        List<Long> answerOptions,
        String solution
) {

}
