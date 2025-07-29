package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionOptionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.BaseMapperTest;
import com.example.indexquiz.example.domain.question.QuestionOption;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionOptionMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            QuestionOption questionOption = new QuestionOption(1L, 1L, "선택지 내용", 1L);

            QuestionOptionEntity questionOptionEntity = questionOptionMapper.mapToEntity(questionOption);

            assertAll(
                    () -> assertThat(questionOptionEntity.getId()).isEqualTo(questionOption.getId()),
                    () -> assertThat(questionOptionEntity.getQuestionId()).isEqualTo(questionOption.getQuestionId()),
                    () -> assertThat(questionOptionEntity.getContent()).isEqualTo(questionOption.getContent()),
                    () -> assertThat(questionOptionEntity.getOrder()).isEqualTo(questionOption.getOrder())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            QuestionOptionEntity questionOptionEntity = new QuestionOptionEntity(1L, 1L, "선택지 내용", 1L);

            QuestionOption questionOption = questionOptionMapper.mapToDomain(questionOptionEntity);

            assertAll(
                    () -> assertThat(questionOption.getId()).isEqualTo(questionOptionEntity.getId()),
                    () -> assertThat(questionOption.getQuestionId()).isEqualTo(questionOptionEntity.getQuestionId()),
                    () -> assertThat(questionOption.getContent()).isEqualTo(questionOptionEntity.getContent()),
                    () -> assertThat(questionOption.getOrder()).isEqualTo(questionOptionEntity.getOrder())
            );
        }
    }
}
