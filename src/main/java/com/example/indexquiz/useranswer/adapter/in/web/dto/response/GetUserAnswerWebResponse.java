package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.List;

public record GetUserAnswerWebResponse(
        boolean isCorrect,
        List<Long> userOptions,
        List<Long> answerOptions,
        String solution
) {

}
