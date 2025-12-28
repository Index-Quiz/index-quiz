package com.example.indexquiz.useranswer.application.port.out.dto;

import java.util.List;

public record DifficultQuestionResponses(
        List<Long> questionIds
) {

}
