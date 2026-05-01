package com.example.indexquiz.useranswer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VisitorProgressTest {

    @Nested
    class CompletedCount {

        @Test
        void 완료된_세트_수를_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of(
                    QuestionSet.A, 5,
                    QuestionSet.C, 7
            ));

            assertThat(progress.completedCount()).isEqualTo(2);
        }

        @Test
        void 완료된_세트가_없으면_0을_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of());

            assertThat(progress.completedCount()).isEqualTo(0);
        }
    }

    @Nested
    class ProgressPercentage {

        @Test
        void 전체_8세트_중_완료_비율을_퍼센트로_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of(
                    QuestionSet.A, 5,
                    QuestionSet.B, 3,
                    QuestionSet.C, 7,
                    QuestionSet.D, 6
            ));

            assertThat(progress.progressPercentage()).isEqualTo(50);
        }

        @Test
        void 모든_세트를_완료하면_100을_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of(
                    QuestionSet.A, 7, QuestionSet.B, 7,
                    QuestionSet.C, 7, QuestionSet.D, 7,
                    QuestionSet.E, 7, QuestionSet.F, 7,
                    QuestionSet.G, 7, QuestionSet.H, 7
            ));

            assertThat(progress.progressPercentage()).isEqualTo(100);
        }

        @Test
        void 완료된_세트가_없으면_0을_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of());

            assertThat(progress.progressPercentage()).isEqualTo(0);
        }
    }

    @Nested
    class GetCompletedSetBestScore {

        @Test
        void 불변_맵을_반환한다() {
            VisitorProgress progress = new VisitorProgress(Map.of(QuestionSet.A, 5));

            assertAll(
                    () -> assertThat(progress.getCompletedSetBestScore()).containsEntry(QuestionSet.A, 5),
                    () -> assertThat(progress.getCompletedSetBestScore()).hasSize(1)
            );
        }
    }
}
