package com.example.indexquiz.useranswer.application.port.in.dto;

import java.util.List;

public record SaveUserAnswerRequest(
        List<Long> options
) {
}
