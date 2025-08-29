package com.example.indexquiz.useranswer.application.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserAnswerDtoMapperTest {

    private UserAnswerDtoMapper userAnswerDtoMapper;

    @BeforeEach
    void setUp() {
        userAnswerDtoMapper = Mappers.getMapper(UserAnswerDtoMapper.class);
    }

    @Nested
    class MapToSaveUserAnswerResponse {

        @Test
        void 도메인을_userAnswerResponse_dto로_매핑할_수_있다() {
            String submitId = UUID.randomUUID().toString();
            UserAnswers userAnswers = new UserAnswers(List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(1L, 2L, submitId)
            ));

            SaveUserAnswerResponse applicationDto = userAnswerDtoMapper.mapToSaveUserAnswerResponse(userAnswers);

            assertThat(applicationDto.submitId()).isEqualTo(submitId);
        }
    }

    @Nested
    class MapToGetUserAnswerResponse {

        @Test
        void 도메인을_userAnswerResponse_dto로_매핑할_수_있다() {
            Question question = DomainFixture.getQuestion(1);
            Answers answers = DomainFixture.getAnswers(question.getId(), List.of(1L, 2L));
            UserAnswers userAnswers = new UserAnswers(question.getId(), List.of(1L, 2L));
            Solution solution = new Solution(1L, question.getId(), "해설");

            GetUserAnswerResponse applicationDto = userAnswerDtoMapper.mapToGetUserAnswerResponse(userAnswers, answers,
                    solution);

            assertAll(
                    () -> assertThat(applicationDto.isCorrect()).isEqualTo(
                            userAnswers.isCorrect(answers.getAnswerOptions())),
                    () -> assertThat(applicationDto.userOptions()).containsExactlyElementsOf(
                            userAnswers.getUserOptions()),
                    () -> assertThat(applicationDto.answerOptions()).containsExactlyElementsOf(
                            answers.getAnswerOptions()),
                    () -> assertThat(applicationDto.solution()).isEqualTo(solution.getDescription())
            );
        }
    }
}
