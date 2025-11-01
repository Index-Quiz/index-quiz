package com.example.indexquiz.useranswer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserAnswersTest {

    @Nested
    class isCorrect {

        @Test
        void 유저답변이_정답인지_판단할_수_있다() {
            List<Long> answerOptions = List.of(1L, 2L);
            String submitId = UUID.randomUUID().toString();
            UserAnswers userAnswers = new UserAnswers(List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(1L, 2L, submitId)
            ));

            assertThat(userAnswers.isCorrect(answerOptions)).isTrue();
        }

        @Test
        void 유저답변이_오답인지_판단할_수_있다() {
            List<Long> answerOptions = List.of(1L, 3L);
            String submitId = UUID.randomUUID().toString();
            UserAnswers userAnswers = new UserAnswers(List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(1L, 2L, submitId)
            ));

            assertThat(userAnswers.isCorrect(answerOptions)).isFalse();
        }
    }


    @Nested
    class GetQuestionId {

        @Test
        void 유저답변의_질문_아이디를_가져온다() {
            long questionId = 1L;
            String submitId = UUID.randomUUID().toString();
            List<UserAnswer> userAnswers = List.of(
                    new UserAnswer(questionId, 1L, submitId),
                    new UserAnswer(questionId, 2L, submitId)
            );
            UserAnswers validuserAnswers = new UserAnswers(userAnswers);

            assertThat(validuserAnswers.getQuestionId()).isEqualTo(questionId);
        }

        @Test
        void 유저답변이_없으면_오류가_발생한다() {
            List<UserAnswer> blankUserAnswers = new ArrayList<>();
            UserAnswers invalidUserAnswers = new UserAnswers(blankUserAnswers);

            assertThatThrownBy(invalidUserAnswers::getQuestionId)
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.INVALID_USER_ANSWERS.getMessage());
        }

        @Test
        void 다른_질문에_대한_유저답변이_있다면_오류가_발생한다() {
            String submitId = UUID.randomUUID().toString();
            List<UserAnswer> otherSubmitIdUserAnswers = List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(2L, 1L, submitId)
            );
            UserAnswers invalidUserAnswers = new UserAnswers(otherSubmitIdUserAnswers);

            assertThatThrownBy(invalidUserAnswers::getQuestionId)
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.INVALID_USER_ANSWERS.getMessage());
        }
    }

    @Nested
    class GetSubmitId {

        @Test
        void 유저답변의_제출_아이디를_가져온다() {
            String submitId = UUID.randomUUID().toString();
            List<UserAnswer> userAnswers = List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(1L, 2L, submitId)
            );
            UserAnswers validuserAnswers = new UserAnswers(userAnswers);

            assertThat(validuserAnswers.getSubmitId()).isEqualTo(submitId);
        }

        @Test
        void 유저답변이_없으면_오류가_발생한다() {
            List<UserAnswer> blankUserAnswers = new ArrayList<>();
            UserAnswers invalidUserAnswers = new UserAnswers(blankUserAnswers);

            assertThatThrownBy(invalidUserAnswers::getSubmitId)
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.INVALID_USER_ANSWERS.getMessage());
        }

        @Test
        void 다른_제출아이디를_가진_유저답변이_있다면_오류가_발생한다() {
            List<UserAnswer> otherSubmitIdUserAnswers = List.of(
                    new UserAnswer(1L, 1L, "submitId1"),
                    new UserAnswer(1L, 1L, "submitId2")
            );
            UserAnswers invalidUserAnswers = new UserAnswers(otherSubmitIdUserAnswers);

            assertThatThrownBy(invalidUserAnswers::getSubmitId)
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.INVALID_USER_ANSWERS.getMessage());
        }
    }
}
