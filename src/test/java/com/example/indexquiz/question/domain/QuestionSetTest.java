package com.example.indexquiz.question.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class QuestionSetTest {

    @Nested
    class ValidateScore {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 3, 7})
        void 유효_범위_내의_점수는_검증을_통과한다(int validScore) {
            assertDoesNotThrow(() -> QuestionSet.A.validateScore(validScore));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 8, 15, 100})
        void 유효_범위를_벗어난_점수는_예외가_발생한다(int invalidScore) {
            assertThatThrownBy(() -> QuestionSet.A.validateScore(invalidScore))
                    .isInstanceOf(IndexQuizException.class)
                    .satisfies(exception -> {
                        IndexQuizException ex = (IndexQuizException) exception;
                        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SCORE_OUT_OF_RANGE);
                    });
        }
    }

    @Nested
    class FindByQuestionOrder {

        @ParameterizedTest
        @CsvSource({
                "1, A", "7, A",
                "8, B", "14, B",
                "15, C", "21, C",
                "22, D", "28, D",
                "29, E", "35, E",
                "36, F", "42, F",
                "43, G", "49, G",
                "50, H", "56, H"
        })
        void 문제_순서로_퀴즈_세트를_찾는다(long questionOrder, String expectedSet) {
            assertThat(QuestionSet.findByQuestionOrder(questionOrder))
                    .hasValue(QuestionSet.valueOf(expectedSet));
        }

        @Test
        void BEST_DIFFICULT_범위의_문제는_빈_Optional을_반환한다() {
            assertThat(QuestionSet.findByQuestionOrder(57)).isEmpty();
        }

        @Test
        void 범위를_벗어난_문제는_빈_Optional을_반환한다() {
            assertThat(QuestionSet.findByQuestionOrder(100)).isEmpty();
        }
    }
}
