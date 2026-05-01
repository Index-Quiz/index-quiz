package com.example.indexquiz.useranswer.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.out.persistence.VisitorQuestionCompletionEntity;
import com.example.indexquiz.useranswer.domain.VisitorQuestionCompletion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class VisitorQuestionCompletionMapperTest {

    private VisitorQuestionCompletionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(VisitorQuestionCompletionMapper.class);
    }

    @Nested
    class MapToEntity {

        @Test
        void 도메인을_엔티티로_매핑할_수_있다() {
            VisitorQuestionCompletion domain = new VisitorQuestionCompletion("visitor-1", 1L, QuestionSet.A);

            VisitorQuestionCompletionEntity entity = mapper.mapToEntity(domain);

            assertAll(
                    () -> assertThat(entity.getVisitorId()).isEqualTo("visitor-1"),
                    () -> assertThat(entity.getQuestionId()).isEqualTo(1L),
                    () -> assertThat(entity.getQuestionSet()).isEqualTo(QuestionSet.A)
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티를_도메인으로_매핑할_수_있다() {
            VisitorQuestionCompletionEntity entity = new VisitorQuestionCompletionEntity(
                    1L, "visitor-1", 5L, QuestionSet.B);

            VisitorQuestionCompletion domain = mapper.mapToDomain(entity);

            assertAll(
                    () -> assertThat(domain.getId()).isEqualTo(1L),
                    () -> assertThat(domain.getVisitorId()).isEqualTo("visitor-1"),
                    () -> assertThat(domain.getQuestionId()).isEqualTo(5L),
                    () -> assertThat(domain.getQuestionSet()).isEqualTo(QuestionSet.B)
            );
        }
    }
}
