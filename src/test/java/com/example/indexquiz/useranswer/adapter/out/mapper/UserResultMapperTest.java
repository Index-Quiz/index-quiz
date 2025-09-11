package com.example.indexquiz.useranswer.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.out.persistence.UserResultEntity;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserResultMapperTest {

    private UserResultMapper userResultMapper;

    @BeforeEach
    void setUp() {
        userResultMapper = Mappers.getMapper(UserResultMapper.class);
    }

    @Nested
    class MapToDomain {

        @Test
        void 유저_성적_엔티티를_유저_성적_도메인으로_매핑할_수_있다() {
            UserResultEntity userResultEntity = new UserResultEntity(1L, QuestionSet.A, 10);

            UserResult userResult = userResultMapper.mapToUserResult(userResultEntity);

            assertAll(
                    () -> assertThat(userResult.getId()).isEqualTo(userResultEntity.getId()),
                    () -> assertThat(userResult.getQuestionSet()).isEqualTo(userResultEntity.getQuestionSet()),
                    () -> assertThat(userResult.getScore()).isEqualTo(userResultEntity.getScore())
            );
        }
    }

    @Nested
    class MapToEntity {

        @Test
        void 유저_성적_도메인을_유저_성적_엔티티로_매핑할_수_있다() {
            UserResult userResult = new UserResult(QuestionSet.A, 10);

            UserResultEntity userResultEntity = userResultMapper.mapToUserResultEntity(userResult);

            assertAll(
                    () -> assertThat(userResultEntity.getId()).isEqualTo(userResult.getId()),
                    () -> assertThat(userResultEntity.getQuestionSet()).isEqualTo(userResult.getQuestionSet()),
                    () -> assertThat(userResultEntity.getScore()).isEqualTo(userResult.getScore())
            );
        }
    }
}
