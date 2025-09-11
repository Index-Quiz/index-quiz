package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.answer.application.port.out.GetAnswerPort;
import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.solution.application.port.out.GetSolutionPort;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.adapter.out.persistence.UserResultEntity;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.application.port.in.mapper.UserAnswerDtoMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.application.port.out.dto.GetUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import com.example.indexquiz.useranswer.domain.UserResult;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class UserAnswerServiceTest extends BaseServiceTest {

    @Autowired
    private UserAnswerService userAnswerService;

    @MockitoSpyBean
    private SaveUserAnswerPort saveUserAnswerPort;

    @MockitoSpyBean
    private SaveUserResultPort saveUserResultPort;

    @MockitoSpyBean
    private GetQuestionPort getQuestionPort;

    @MockitoSpyBean
    private GetAnswerPort getAnswerPort;

    @MockitoSpyBean
    private GetSolutionPort getSolutionPort;

    @MockitoSpyBean
    private GetUserAnswerPort getUserAnswerPort;

    @Nested
    class SaveUserAnswers {

        @Test
        void 사용자의_답변을_저장한다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);

            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);
            willReturn(questionWithOptions).given(getQuestionPort).getQuestionWithOptions(anyLong());

            UserAnswers userAnswers = new UserAnswers(question.getId(), List.of(1L, 2L, 3L));
            willReturn(userAnswers).given(saveUserAnswerPort).saveUserAnswers(any());

            // when
            SaveUserAnswerRequest request = new SaveUserAnswerRequest(question.getId(), List.of(1L, 2L, 3L));
            userAnswerService.saveUserAnswers(request);

            // then
            ArgumentCaptor<SaveUserAnswersCommand> captor = ArgumentCaptor.captor();
            then(saveUserAnswerPort).should().saveUserAnswers(captor.capture());
            List<UserAnswer> actualUserAnswers = captor.getValue().getUserAnswers();
            assertAll(
                    () -> assertThat(actualUserAnswers.stream().map(UserAnswer::getQuestionId)).containsExactly(1L, 1L,
                            1L),
                    () -> assertThat(actualUserAnswers.stream().map(UserAnswer::getOptionId)).containsExactly(1L, 2L,
                            3L)
            );
        }
    }

    @Nested
    class GetUserAnswers {

        @Test
        void 사용자_답변의_채점_결과를_가져온다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<Long> options = List.of(1L, 2L);

            UserAnswers userAnswers = new UserAnswers(question.getId(), options);
            willReturn(userAnswers).given(getUserAnswerPort).getBySubmitId(any());

            Answers answers = DomainFixture.getAnswers(question.getId(), options);
            willReturn(answers).given(getAnswerPort).getByQuestionId(question.getId());

            Solution solution = new Solution(1L, question.getId(), "해설");
            willReturn(solution).given(getSolutionPort).getByQuestionId(question.getId());

            // when
            GetUserAnswerRequest request = new GetUserAnswerRequest(userAnswers.getSubmitId());
            GetUserAnswerResponse userAnswerResponse = userAnswerService.getUserAnswer(request);

            // then
            assertAll(
                    () -> assertThat(userAnswerResponse.isCorrect()).isTrue(),
                    () -> assertThat(userAnswerResponse.userOptions()).containsExactlyElementsOf(userAnswers.getUserOptions()),
                    () -> assertThat(userAnswerResponse.answerOptions()).containsExactlyElementsOf(answers.getAnswerOptions()),
                    () -> assertThat(userAnswerResponse.solution()).isEqualTo(solution.getDescription())
            );
        }
    }

    @Nested
    class SaveUserResult {

        @Test
        void 사용자의_성적을_저장한다() {
            // given
            UserResult userResult = new UserResult(QuestionSet.A, 10);
            UserResult savedUserResult = new UserResult(
                    1L,
                    userResult.getQuestionSet(),
                    userResult.getScore()
            );
            willReturn(savedUserResult).given(saveUserResultPort).saveUserResult(userResult);

            // when
            SaveUserResultRequest request = new SaveUserResultRequest(userResult.getQuestionSet(), userResult.getScore());
            SaveUserResultResponse saveUserResultResponse = userAnswerService.saveUserResult(request);

            // then
            assertAll(
                    () -> assertThat(saveUserResultResponse.id()).isEqualTo(savedUserResult.getId()),
                    () -> assertThat(saveUserResultResponse.score()).isEqualTo(savedUserResult.getScore()),
                    () -> assertThat(saveUserResultResponse.questionSetName()).isEqualTo(savedUserResult.getQuestionSet())
            );
        }
    }
}
