package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class UserResultServiceTest extends BaseServiceTest {

    @Autowired
    private UserResultService userResultService;

    @MockitoSpyBean
    private SaveUserResultPort saveUserResultPort;

    @Nested
    class SaveUserResult {

        @Test
        void 사용자의_성적을_저장한다() {
            // given
            UserResult userResult = new UserResult(QuestionSet.A, 5, "test-visitor");
            UserResult savedUserResult = new UserResult(
                    1L,
                    userResult.getQuestionSet(),
                    userResult.getScore(),
                    userResult.getVisitorId()
            );
            willReturn(savedUserResult).given(saveUserResultPort).saveUserResult(userResult);

            // when
            SaveUserResultRequest request = new SaveUserResultRequest(userResult.getQuestionSet(), userResult.getScore(), "test-visitor");
            SaveUserResultResponse saveUserResultResponse = userResultService.saveUserResult(request);

            // then
            assertAll(
                    () -> assertThat(saveUserResultResponse.id()).isEqualTo(savedUserResult.getId()),
                    () -> assertThat(saveUserResultResponse.score()).isEqualTo(savedUserResult.getScore()),
                    () -> assertThat(saveUserResultResponse.questionSetName()).isEqualTo(savedUserResult.getQuestionSet()),
                    () -> assertThat(saveUserResultResponse.scoreDistribution()).hasSize(8),
                    () -> assertThat(saveUserResultResponse.averageScore()).isGreaterThanOrEqualTo(0),
                    () -> assertThat(saveUserResultResponse.topPercentage()).isBetween(0.0, 100.0)
            );
        }

    }
}
