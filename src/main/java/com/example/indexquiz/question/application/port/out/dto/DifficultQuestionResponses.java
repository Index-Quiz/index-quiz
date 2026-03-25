package com.example.indexquiz.question.application.port.out.dto;

import java.util.List;

public record DifficultQuestionResponses(
        List<Long> questionIds
) {

}
