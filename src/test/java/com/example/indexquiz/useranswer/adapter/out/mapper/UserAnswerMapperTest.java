package com.example.indexquiz.useranswer.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.useranswer.adapter.out.persistence.UserAnswerEntity;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserAnswerMapperTest extends BaseMapperTest {

    private UserAnswerMapper userAnswerMapper;

    @BeforeEach
    void setUp() {
        userAnswerMapper = Mappers.getMapper(UserAnswerMapper.class);
    }

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            UserAnswer userAnswer = new UserAnswer(1L, 1L, 1L, "uuid");

            UserAnswerEntity userAnswerEntity = userAnswerMapper.mapToUserAnswerEntity(userAnswer);

            assertAll(
                    () -> assertThat(userAnswerEntity.getId()).isEqualTo(userAnswer.getId()),
                    () -> assertThat(userAnswerEntity.getQuestionId()).isEqualTo(userAnswer.getQuestionId()),
                    () -> assertThat(userAnswerEntity.getOptionId()).isEqualTo(userAnswer.getOptionId()),
                    () -> assertThat(userAnswerEntity.getSubmitId()).isEqualTo(userAnswer.getSubmitId())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            UserAnswerEntity userAnswerEntity = new UserAnswerEntity(1L, 1L, 1L, "uuid");

            UserAnswer userAnswer = userAnswerMapper.mapToUserAnswer(userAnswerEntity);

            assertAll(
                    () -> assertThat(userAnswer.getId()).isEqualTo(userAnswerEntity.getId()),
                    () -> assertThat(userAnswer.getQuestionId()).isEqualTo(userAnswerEntity.getQuestionId()),
                    () -> assertThat(userAnswer.getOptionId()).isEqualTo(userAnswerEntity.getOptionId()),
                    () -> assertThat(userAnswer.getSubmitId()).isEqualTo(userAnswerEntity.getSubmitId())
            );
        }
    }
}
