package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserResultWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserAnswerWebMapperTest {

    private UserAnswerWebMapper userAnswerWebMapper;

    @BeforeEach
    void setUp() {
        userAnswerWebMapper = Mappers.getMapper(UserAnswerWebMapper.class);
    }

    @Nested
    class MapToSaveUserAnswerRequest {

        @Test
        void web_dto에서_어플리케이션_dto로_매핑할_수_있다() {
            SaveUserAnswerWebRequest origin = new SaveUserAnswerWebRequest(1L, List.of(1L, 2L));

            SaveUserAnswerRequest target = userAnswerWebMapper.mapToSaveUserAnswerRequest(origin);

            assertAll(
                    () -> assertThat(target.questionId()).isEqualTo(origin.questionId()),
                    () -> assertThat(target.options()).containsExactlyElementsOf(origin.options())
            );
        }
    }

    @Nested
    class MapToSaveUserAnswerWebResponse {

        @Test
        void 어플리케이션_dto에서_web_dto로_매핑할_수_있다() {
            SaveUserAnswerResponse origin = new SaveUserAnswerResponse("example");

            SaveUserAnswerWebResponse target = userAnswerWebMapper.mapToSaveUserAnswerWebResponse(origin);

            assertThat(target.submitId()).isEqualTo(origin.submitId());
        }
    }

    @Nested
    class MapToGetUserAnswerWebResponse {

        @Test
        void 어플리케이션_dto에서_web_dto로_매핑할_수_있다() {
            GetUserAnswerResponse origin = new GetUserAnswerResponse(
                    true,
                    List.of(1L, 2L),
                    List.of(1L, 2L),
                    "해설"
            );

            GetUserAnswerWebResponse target = userAnswerWebMapper.mapToGetUserAnswerWebResponse(origin);

            assertAll(
                    () -> assertThat(target.isCorrect()).isEqualTo(origin.isCorrect()),
                    () -> assertThat(target.userOptions()).containsExactlyElementsOf(origin.userOptions()),
                    () -> assertThat(target.answerOptions()).containsExactlyElementsOf(origin.answerOptions()),
                    () -> assertThat(target.solution()).isEqualTo(origin.solution())
            );
        }
    }

    @Nested
    class MapToSaveUserResultRequest {

        @Test
        void web_dto에서_어플리케이션_dto로_매핑할_수_있다() {
            SaveUserResultWebRequest origin = new SaveUserResultWebRequest(QuestionSet.A.name(), 14);

            SaveUserResultRequest target = userAnswerWebMapper.mapToSaveUserResultRequest(origin);

            assertAll(
                    () -> assertThat(target.questionSetName().name()).isEqualTo(origin.questionSetName()),
                    () -> assertThat(target.score()).isEqualTo(origin.score())
            );
        }
    }

    @Nested
    class MapToSaveUserResultResponse {

        @Test
        void 어플리케이션_dto에서_web_dto로_매핑할_수_있다() {
            SaveUserResultResponse origin = new SaveUserResultResponse(
                    1L, QuestionSet.A, 10,
                    List.of(1L, 2L, 3L, 5L, 8L, 10L, 4L, 2L),
                    3.52, 42.0
            );

            SaveUserResultWebResponse target = userAnswerWebMapper.mapToSaveUserResultWebResponse(origin);

            assertAll(
                    () -> assertThat(target.id()).isEqualTo(origin.id()),
                    () -> assertThat(target.questionSetName()).isEqualTo(origin.questionSetName().name()),
                    () -> assertThat(target.score()).isEqualTo(origin.score()),
                    () -> assertThat(target.scoreDistribution()).isEqualTo(origin.scoreDistribution()),
                    () -> assertThat(target.averageScore()).isEqualTo(origin.averageScore()),
                    () -> assertThat(target.topPercentage()).isEqualTo(origin.topPercentage())
            );
        }
    }
}
