package com.example.indexquiz.useranswer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ScoreCountsTest {

    @Nested
    class ToDistribution {

        @Test
        void 정상_분포를_계산한다() {
            // given
            List<ScoreCount> counts = List.of(
                    new ScoreCount(0, 1),
                    new ScoreCount(3, 5),
                    new ScoreCount(5, 3),
                    new ScoreCount(7, 2)
            );
            ScoreCounts scoreCounts = new ScoreCounts(counts);

            // when
            ScoreDistribution result = scoreCounts.toDistribution(5, 7);

            // then
            assertAll(
                    () -> assertThat(result.distribution()).hasSize(8),
                    () -> assertThat(result.distribution().get(0)).isEqualTo(1),
                    () -> assertThat(result.distribution().get(3)).isEqualTo(5),
                    () -> assertThat(result.distribution().get(5)).isEqualTo(3),
                    () -> assertThat(result.distribution().get(7)).isEqualTo(2),
                    () -> assertThat(result.distribution().get(1)).isEqualTo(0),
                    () -> assertThat(result.averageScore()).isGreaterThan(0),
                    () -> assertThat(result.topPercentage()).isBetween(0.0, 100.0)
            );
        }

        @Test
        void 빈_리스트일_때_기본값을_반환한다() {
            // given
            ScoreCounts scoreCounts = new ScoreCounts(List.of());

            // when
            ScoreDistribution result = scoreCounts.toDistribution(3, 7);

            // then
            assertAll(
                    () -> assertThat(result.distribution()).hasSize(8),
                    () -> assertThat(result.distribution()).allMatch(count -> count == 0),
                    () -> assertThat(result.averageScore()).isEqualTo(0.0),
                    () -> assertThat(result.topPercentage()).isEqualTo(100.0)
            );
        }

        @Test
        void 경계값_score_0에서_상위_퍼센트는_100이다() {
            // given
            List<ScoreCount> counts = List.of(
                    new ScoreCount(0, 2),
                    new ScoreCount(3, 3),
                    new ScoreCount(7, 1)
            );
            ScoreCounts scoreCounts = new ScoreCounts(counts);

            // when
            ScoreDistribution result = scoreCounts.toDistribution(0, 7);

            // then
            assertThat(result.topPercentage()).isEqualTo(100.0);
        }

        @Test
        void 경계값_score_maxScore에서_해당_점수_비율만_상위에_포함된다() {
            // given
            List<ScoreCount> counts = List.of(
                    new ScoreCount(0, 5),
                    new ScoreCount(3, 3),
                    new ScoreCount(7, 2)
            );
            ScoreCounts scoreCounts = new ScoreCounts(counts);

            // when
            ScoreDistribution result = scoreCounts.toDistribution(7, 7);

            // then
            assertThat(result.topPercentage()).isEqualTo(20.0);
        }

        @Test
        void 범위_밖_score는_통계에서_제외된다() {
            // given
            List<ScoreCount> counts = List.of(
                    new ScoreCount(-1, 10),
                    new ScoreCount(3, 4),
                    new ScoreCount(8, 10),
                    new ScoreCount(15, 10)
            );
            ScoreCounts scoreCounts = new ScoreCounts(counts);

            // when
            ScoreDistribution result = scoreCounts.toDistribution(3, 7);

            // then
            assertAll(
                    () -> assertThat(result.distribution().get(3)).isEqualTo(4),
                    () -> assertThat(result.averageScore()).isEqualTo(3.0),
                    () -> assertThat(result.topPercentage()).isEqualTo(100.0)
            );
        }

        @Test
        void 평균_점수를_정확하게_계산한다() {
            // given: (2*1 + 4*3 + 6*1) / 5 = 20/5 = 4.0
            List<ScoreCount> counts = List.of(
                    new ScoreCount(2, 1),
                    new ScoreCount(4, 3),
                    new ScoreCount(6, 1)
            );
            ScoreCounts scoreCounts = new ScoreCounts(counts);

            // when
            ScoreDistribution result = scoreCounts.toDistribution(4, 7);

            // then
            assertThat(result.averageScore()).isEqualTo(4.0);
        }
    }
}
