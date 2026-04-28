package com.example.indexquiz.useranswer.domain;

import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScoreCounts {

    private static final int MAX_SCORE = 7;

    private final List<ScoreCount> values;

    public ScoreDistribution toDistribution(int userScore) {
        List<ScoreCount> validCounts = filterValidScores();

        List<Long> distribution = buildDistribution(validCounts);
        long totalCount = sumTotalCount(validCounts);
        long scoredGreaterOrEqual = sumGreaterOrEqual(validCounts, userScore);
        long totalScoreSum = sumScores(validCounts);

        double average = calculateAverage(totalScoreSum, totalCount);
        double topPercentage = calculateTopPercentage(scoredGreaterOrEqual, totalCount);

        return new ScoreDistribution(
                distribution,
                Math.round(average * 100.0) / 100.0,
                Math.round(topPercentage * 10.0) / 10.0
        );
    }

    private List<ScoreCount> filterValidScores() {
        return values.stream()
                .filter(sc -> sc.score() >= 0 && sc.score() <= MAX_SCORE)
                .toList();
    }

    private List<Long> buildDistribution(List<ScoreCount> validCounts) {
        return IntStream.rangeClosed(0, MAX_SCORE)
                .mapToLong(score -> validCounts.stream()
                        .filter(sc -> sc.score() == score)
                        .mapToLong(ScoreCount::count)
                        .sum())
                .boxed()
                .toList();
    }

    private long sumTotalCount(List<ScoreCount> validCounts) {
        return validCounts.stream()
                .mapToLong(ScoreCount::count)
                .sum();
    }

    private long sumGreaterOrEqual(List<ScoreCount> validCounts, int userScore) {
        return validCounts.stream()
                .filter(sc -> sc.score() >= userScore)
                .mapToLong(ScoreCount::count)
                .sum();
    }

    private long sumScores(List<ScoreCount> validCounts) {
        return validCounts.stream()
                .mapToLong(sc -> Long.valueOf(sc.score()) * sc.count())
                .sum();
    }

    private double calculateAverage(long totalScoreSum, long totalCount) {
        if (totalCount == 0) {
            return 0.0;
        }
        return Double.valueOf(totalScoreSum) / totalCount;
    }

    private double calculateTopPercentage(long scoredGreaterOrEqual, long totalCount) {
        if (totalCount == 0) {
            return 100.0;
        }
        return (Double.valueOf(scoredGreaterOrEqual) / totalCount) * 100.0;
    }
}
