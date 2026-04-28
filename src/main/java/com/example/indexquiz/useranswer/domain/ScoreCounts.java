package com.example.indexquiz.useranswer.domain;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScoreCounts {

    private static final int MAX_SCORE = 7;

    private final List<ScoreCount> values;

    public ScoreDistribution toDistribution(int userScore) {
        long[] distribution = new long[MAX_SCORE + 1];
        long totalCount = 0;
        long scoredGreaterOrEqual = 0;
        long totalScoreSum = 0;

        for (ScoreCount scoreCount : values) {
            int score = scoreCount.score();
            long count = scoreCount.count();

            if (score >= 0 && score <= MAX_SCORE) {
                distribution[score] = count;
            }

            totalCount += count;
            totalScoreSum += (long) score * count;

            if (score >= userScore) {
                scoredGreaterOrEqual += count;
            }
        }

        double average = totalCount > 0 ? (double) totalScoreSum / totalCount : 0.0;
        double topPercentage = totalCount > 0 ? ((double) scoredGreaterOrEqual / totalCount) * 100.0 : 100.0;

        return new ScoreDistribution(
                Arrays.stream(distribution).boxed().toList(),
                Math.round(average * 100.0) / 100.0,
                Math.round(topPercentage * 10.0) / 10.0
        );
    }
}
