package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.BaseMapperTest;
import com.example.indexquiz.example.domain.question.Question;
import com.example.indexquiz.example.domain.question.QuestionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            Question question = new Question(1L, QuestionType.SINGLE_CHOICE, "질문 내용", 1L);

            QuestionEntity questionEntity = questionMapper.mapToEntity(question);

            assertAll(
                    () -> assertThat(questionEntity.getId()).isEqualTo(question.getId()),
                    () -> assertThat(questionEntity.getType()).isEqualTo(question.getType()),
                    () -> assertThat(questionEntity.getContent()).isEqualTo(question.getContent()),
                    () -> assertThat(questionEntity.getOrder()).isEqualTo(question.getOrder())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            QuestionEntity questionEntity = new QuestionEntity(1L, QuestionType.SINGLE_CHOICE, "질문 내용", 1L);

            Question question = questionMapper.mapToDomain(questionEntity);

            assertAll(
                    () -> assertThat(question.getId()).isEqualTo(questionEntity.getId()),
                    () -> assertThat(question.getType()).isEqualTo(questionEntity.getType()),
                    () -> assertThat(question.getContent()).isEqualTo(questionEntity.getContent()),
                    () -> assertThat(question.getOrder()).isEqualTo(questionEntity.getOrder())
            );
        }
    }
}
