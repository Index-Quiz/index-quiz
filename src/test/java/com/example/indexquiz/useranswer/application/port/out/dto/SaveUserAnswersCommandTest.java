package com.example.indexquiz.useranswer.application.port.out.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SaveUserAnswersCommandTest {

    @Nested
    class SaveUserAnswersCommandConstructor {

        @Test
        void 객체를_생성할_수_있다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);

            // when
            SaveUserAnswersCommand command = new SaveUserAnswersCommand(questionWithOptions, List.of(1L));

            // then
            List<UserAnswer> userAnswers = command.getUserAnswers();
            assertAll(
                    () -> assertThat(userAnswers.stream().map(UserAnswer::getQuestionId)).containsExactly(1L),
                    () -> assertThat(userAnswers.stream().map(UserAnswer::getOptionId)).containsExactly(1L)
            );
        }

        @Test
        void 객체를_생성할_때_질문_선택지_유효성_검사_실패_시_예외를_반환한다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);

            // when & then
            assertThatThrownBy(() -> new SaveUserAnswersCommand(questionWithOptions, List.of(1L, 2L, 999L)))
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.QUESTION_OPTION_INVALID.getMessage());
        }
    }
}
