package com.example.indexquiz.useranswer.application.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
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
}
