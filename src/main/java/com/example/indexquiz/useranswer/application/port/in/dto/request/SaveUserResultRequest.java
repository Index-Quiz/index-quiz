package com.example.indexquiz.useranswer.application.port.in.dto.request;

import com.example.indexquiz.question.domain.QuestionSet;

public record SaveUserResultRequest(
        QuestionSet questionSetName,
        int score,
        String visitorId
) {

}
