package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UserAnswerServiceTest extends BaseServiceTest {

    @InjectMocks
    private UserAnswerService userAnswerService;

    @Mock
    private SaveUserAnswerPort saveUserAnswerPort;

    @Mock
    private GetQuestionPort getQuestionPort;

    @Nested
    class SaveUserAnswers {

        @Test
        void 사용자의_답변을_저장한다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);
            given(getQuestionPort.getQuestionWithOptions(anyLong())).willReturn(questionWithOptions);
            willDoNothing().given(saveUserAnswerPort).saveUserAnswers(any());

            // when
            SaveUserAnswerRequest request = new SaveUserAnswerRequest(List.of(1L, 2L, 3L));
            userAnswerService.saveUserAnswers(1L, request);

            // then
            ArgumentCaptor<SaveUserAnswersCommand> captor = ArgumentCaptor.captor();
            then(saveUserAnswerPort).should().saveUserAnswers(captor.capture());
            List<UserAnswer> userAnswers = captor.getValue().getUserAnswers();
            assertAll(
                    () -> assertThat(userAnswers.stream().map(UserAnswer::getQuestionId)).containsExactly(1L, 1L, 1L),
                    () -> assertThat(userAnswers.stream().map(UserAnswer::getOptionId)).containsExactly(1L, 2L, 3L)
            );
        }
    }
}
