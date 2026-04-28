package com.example.indexquiz.question.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
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
}
