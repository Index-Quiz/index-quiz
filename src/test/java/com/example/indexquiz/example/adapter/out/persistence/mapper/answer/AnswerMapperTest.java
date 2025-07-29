package com.example.indexquiz.example.adapter.out.persistence.mapper.answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.answer.AnswerEntity;
import com.example.indexquiz.example.domain.answer.Answer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AnswerMapperTest extends BaseMapperTest {

    @Autowired
    private AnswerMapper answerMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            Answer answer = new Answer(1L, 1L, 1L);

            AnswerEntity answerEntity = answerMapper.mapToEntity(answer);

            assertAll(
                    () -> assertThat(answerEntity.getId()).isEqualTo(answer.getId()),
                    () -> assertThat(answerEntity.getQuestionId()).isEqualTo(answer.getQuestionId()),
                    () -> assertThat(answerEntity.getOptionId()).isEqualTo(answer.getOptionId())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            AnswerEntity answerEntity = new AnswerEntity(1L, 1L, 1L);

            Answer answer = answerMapper.mapToDomain(answerEntity);

            assertAll(
                    () -> assertThat(answer.getId()).isEqualTo(answerEntity.getId()),
                    () -> assertThat(answer.getQuestionId()).isEqualTo(answerEntity.getQuestionId()),
                    () -> assertThat(answer.getOptionId()).isEqualTo(answerEntity.getOptionId())
            );
        }
    }
}
