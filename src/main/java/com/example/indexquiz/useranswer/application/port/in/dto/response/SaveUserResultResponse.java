package com.example.indexquiz.useranswer.application.port.in.dto.response;

import com.example.indexquiz.question.domain.QuestionSet;

public record SaveUserResultResponse(
        long id,
        QuestionSet questionSetName,
        int score
) {

}
