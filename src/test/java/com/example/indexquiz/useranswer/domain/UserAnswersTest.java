package com.example.indexquiz.useranswer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserAnswersTest {

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
