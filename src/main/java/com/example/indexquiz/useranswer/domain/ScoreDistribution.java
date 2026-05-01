package com.example.indexquiz.useranswer.domain;

import java.util.List;

public record ScoreDistribution(
        List<Long> distribution,
        double averageScore,
        double topPercentage
) {

}
