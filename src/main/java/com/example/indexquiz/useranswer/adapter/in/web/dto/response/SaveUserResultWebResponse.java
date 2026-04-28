package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.List;

public record SaveUserResultWebResponse(
        long id,
        String questionSetName,
        int score,
        List<Long> scoreDistribution,
        double averageScore,
        double topPercentage
) {

}
