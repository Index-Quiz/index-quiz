package com.example.indexquiz.useranswer.application.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;
import com.example.indexquiz.useranswer.domain.UserResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserResultDtoMapperTest {

    private UserResultDtoMapper userResultDtoMapper;

    @BeforeEach
    void setUp() {
        userResultDtoMapper = new UserResultDtoMapper();
    }

    @Nested
    class MapToSaveUserResultResponse {

        @Test
        void 도메인을_saveUserResultResponse_dto로_매핑할_수_있다() {
            UserResult userResult = new UserResult(1L, QuestionSet.A, 5);
            ScoreDistribution distribution = new ScoreDistribution(
                    List.of(1L, 2L, 3L, 5L, 8L, 10L, 4L, 2L),
                    3.52,
                    42.0
            );

            SaveUserResultResponse applicationDto = userResultDtoMapper.mapToSaveUserResultResponse(userResult, distribution);

            assertAll(
                    () -> assertThat(applicationDto.id()).isEqualTo(userResult.getId()),
                    () -> assertThat(applicationDto.questionSetName()).isEqualTo(userResult.getQuestionSet()),
                    () -> assertThat(applicationDto.score()).isEqualTo(userResult.getScore()),
                    () -> assertThat(applicationDto.scoreDistribution()).isEqualTo(distribution.distribution()),
                    () -> assertThat(applicationDto.averageScore()).isEqualTo(distribution.averageScore()),
                    () -> assertThat(applicationDto.topPercentage()).isEqualTo(distribution.topPercentage())
            );
        }
    }

}
