package com.example.indexquiz.question.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class QuestionOptionMapperTest extends BaseMapperTest {

    private QuestionOptionMapper questionOptionMapper;

    @BeforeEach
    void setUp() {
        questionOptionMapper = Mappers.getMapper(QuestionOptionMapper.class);
    }

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            QuestionOption questionOption = new QuestionOption(1L, 1L, "선택지 내용", 1L);

            QuestionOptionEntity questionOptionEntity = questionOptionMapper.mapToQuestionOptionEntity(questionOption);

            assertAll(
                    () -> assertThat(questionOptionEntity.getId()).isEqualTo(questionOption.getId()),
                    () -> assertThat(questionOptionEntity.getQuestionId()).isEqualTo(questionOption.getQuestionId()),
                    () -> assertThat(questionOptionEntity.getContent()).isEqualTo(questionOption.getContent()),
                    () -> assertThat(questionOptionEntity.getOptionOrder()).isEqualTo(questionOption.getOptionOrder())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            QuestionOptionEntity questionOptionEntity = new QuestionOptionEntity(1L, 1L, "선택지 내용", 1L);

            QuestionOption questionOption = questionOptionMapper.mapToQuestionOption(questionOptionEntity);

            assertAll(
                    () -> assertThat(questionOption.getId()).isEqualTo(questionOptionEntity.getId()),
                    () -> assertThat(questionOption.getQuestionId()).isEqualTo(questionOptionEntity.getQuestionId()),
                    () -> assertThat(questionOption.getContent()).isEqualTo(questionOptionEntity.getContent()),
                    () -> assertThat(questionOption.getOptionOrder()).isEqualTo(questionOptionEntity.getOptionOrder())
            );
        }
    }
}
