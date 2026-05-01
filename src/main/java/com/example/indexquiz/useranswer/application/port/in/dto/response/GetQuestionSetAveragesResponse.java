package com.example.indexquiz.useranswer.application.port.in.dto.response;

import java.util.List;

public record GetQuestionSetAveragesResponse(
        List<QuestionSetAverage> averages
) {

}
