package com.example.indexquiz.useranswer.domain;

import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScoreCounts {

    private final List<ScoreCount> values;

    public ScoreDistribution toDistribution(int userScore, int maxScore) {
        List<ScoreCount> validCounts = filterValidScores(maxScore);

        List<Long> distribution = buildDistribution(validCounts, maxScore);
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

    private List<ScoreCount> filterValidScores(int maxScore) {
        return values.stream()
                .filter(sc -> sc.score() >= 0 && sc.score() <= maxScore)
                .toList();
    }

    private List<Long> buildDistribution(List<ScoreCount> validCounts, int maxScore) {
        return IntStream.rangeClosed(0, maxScore)
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
        return (double) totalScoreSum / totalCount;
    }

    private double calculateTopPercentage(long scoredGreaterOrEqual, long totalCount) {
        if (totalCount == 0) {
            return 100.0;
        }
        return ((double) scoredGreaterOrEqual / totalCount) * 100.0;
    }
}
