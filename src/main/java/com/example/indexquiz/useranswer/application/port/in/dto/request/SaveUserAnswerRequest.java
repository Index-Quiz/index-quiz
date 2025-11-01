package com.example.indexquiz.useranswer.application.port.in.dto.request;

import java.util.List;

public record SaveUserAnswerRequest(
        long questionId,
        List<Long> options
) {

}
