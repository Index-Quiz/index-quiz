package com.example.indexquiz.useranswer.application.port.in.dto.response;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;

public record SaveUserResultResponse(
        long id,
        QuestionSet questionSetName,
        int score,
        List<Long> scoreDistribution,
        double averageScore,
        double topPercentage
) {

}
