package com.example.indexquiz.useranswer.application.port.in.dto.response;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Map;

public record GetQuestionSetAveragesResponse(
        Map<QuestionSet, Double> averages
) {

}
