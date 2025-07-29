package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionImageEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.BaseMapperTest;
import com.example.indexquiz.example.domain.question.QuestionImage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionImageMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionImageMapper questionImageMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            QuestionImage questionImage = new QuestionImage(1L, 1L, "http://example.com");

            QuestionImageEntity imageEntity = questionImageMapper.mapToEntity(questionImage);

            assertAll(
                    () -> assertThat(imageEntity.getId()).isEqualTo(questionImage.getId()),
                    () -> assertThat(imageEntity.getQuestionId()).isEqualTo(questionImage.getQuestionId()),
                    () -> assertThat(imageEntity.getUrl()).isEqualTo(questionImage.getUrl())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            QuestionImageEntity questionImageEntity = new QuestionImageEntity(1L, 1L, "http://example.com");

            QuestionImage questionImage = questionImageMapper.mapToDomain(questionImageEntity);

            assertAll(
                    () -> assertThat(questionImageEntity.getId()).isEqualTo(questionImage.getId()),
                    () -> assertThat(questionImageEntity.getQuestionId()).isEqualTo(questionImage.getQuestionId()),
                    () -> assertThat(questionImageEntity.getUrl()).isEqualTo(questionImage.getUrl())
            );
        }
    }
}
