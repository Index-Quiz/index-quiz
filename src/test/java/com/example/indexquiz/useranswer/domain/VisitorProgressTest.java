package com.example.indexquiz.useranswer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VisitorProgressTest {

    @Nested
    class ProgressPercentage {

        @Test
        void 전체_8세트_중_완료_비율을_퍼센트로_반환한다() {
            VisitorProgress progress = new VisitorProgress(List.of(
                    new SetBestScore(QuestionSet.A, 5),
                    new SetBestScore(QuestionSet.B, 3),
                    new SetBestScore(QuestionSet.C, 7),
                    new SetBestScore(QuestionSet.D, 6)
            ));

            assertThat(progress.progressPercentage()).isEqualTo(50);
        }

        @Test
        void 모든_세트를_완료하면_100을_반환한다() {
            VisitorProgress progress = new VisitorProgress(List.of(
                    new SetBestScore(QuestionSet.A, 7),
                    new SetBestScore(QuestionSet.B, 7),
                    new SetBestScore(QuestionSet.C, 7),
                    new SetBestScore(QuestionSet.D, 7),
                    new SetBestScore(QuestionSet.E, 7),
                    new SetBestScore(QuestionSet.F, 7),
                    new SetBestScore(QuestionSet.G, 7),
                    new SetBestScore(QuestionSet.H, 7)
            ));

            assertThat(progress.progressPercentage()).isEqualTo(100);
        }

        @Test
        void 완료된_세트가_없으면_0을_반환한다() {
            VisitorProgress progress = new VisitorProgress(List.of());

            assertThat(progress.progressPercentage()).isEqualTo(0);
        }
    }

    @Nested
    class GetCompletedSets {

        @Test
        void 불변_리스트를_반환한다() {
            VisitorProgress progress = new VisitorProgress(List.of(
                    new SetBestScore(QuestionSet.A, 5),
                    new SetBestScore(QuestionSet.C, 7)
            ));

            assertThat(progress.getCompletedSets()).containsExactly(
                    new SetBestScore(QuestionSet.A, 5),
                    new SetBestScore(QuestionSet.C, 7)
            );
        }
    }
}
