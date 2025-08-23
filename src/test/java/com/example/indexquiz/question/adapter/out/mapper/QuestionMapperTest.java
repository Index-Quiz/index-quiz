package com.example.indexquiz.question.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.question.adapter.out.persistence.QuestionEntity;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class QuestionMapperTest extends BaseMapperTest {

    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        questionMapper = Mappers.getMapper(QuestionMapper.class);
    }

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            Question question = new Question(1L, QuestionType.SINGLE_CHOICE, "질문 내용", 1L);

            QuestionEntity questionEntity = questionMapper.mapToQuestionEntity(question);

            assertAll(
                    () -> assertThat(questionEntity.getId()).isEqualTo(question.getId()),
                    () -> assertThat(questionEntity.getType()).isEqualTo(question.getType()),
                    () -> assertThat(questionEntity.getContent()).isEqualTo(question.getContent()),
                    () -> assertThat(questionEntity.getQuestionOrder()).isEqualTo(question.getQuestionOrder())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            QuestionEntity questionEntity = new QuestionEntity(1L, QuestionType.SINGLE_CHOICE, "질문 내용", 1L);

            Question question = questionMapper.mapToQuestion(questionEntity);

            assertAll(
                    () -> assertThat(question.getId()).isEqualTo(questionEntity.getId()),
                    () -> assertThat(question.getType()).isEqualTo(questionEntity.getType()),
                    () -> assertThat(question.getContent()).isEqualTo(questionEntity.getContent()),
                    () -> assertThat(question.getQuestionOrder()).isEqualTo(questionEntity.getQuestionOrder())
            );
        }
    }
}
