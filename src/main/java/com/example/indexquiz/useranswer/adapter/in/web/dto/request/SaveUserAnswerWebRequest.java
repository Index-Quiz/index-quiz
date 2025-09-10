package com.example.indexquiz.useranswer.adapter.in.web.dto.request;

import java.util.List;

public record SaveUserAnswerWebRequest(
        long questionId,
        List<Long> options
) {

}
