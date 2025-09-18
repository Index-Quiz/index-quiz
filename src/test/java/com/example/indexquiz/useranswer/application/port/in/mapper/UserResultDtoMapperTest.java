package com.example.indexquiz.useranswer.application.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserResultDtoMapperTest {

    private UserResultDtoMapper userResultDtoMapper;

    @BeforeEach
    void setUp() {
        userResultDtoMapper = Mappers.getMapper(UserResultDtoMapper.class);
    }

    @Nested
    class MapToSaveUserResultResponse {

        @Test
        void 도메인을_saveUserResultResponse_dto로_매핑할_수_있다() {
            UserResult userResult = new UserResult(1L, QuestionSet.A, 10);

            SaveUserResultResponse applicationDto = userResultDtoMapper.mapToSaveUserResultResponse(userResult);

            assertAll(
                    () -> assertThat(applicationDto.id()).isEqualTo(userResult.getId()),
                    () -> assertThat(applicationDto.questionSetName()).isEqualTo(userResult.getQuestionSet()),
                    () -> assertThat(applicationDto.score()).isEqualTo(userResult.getScore())
            );
        }
    }

}
