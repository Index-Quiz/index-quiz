package com.example.indexquiz.question.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.common.EntityFixture;
import com.example.indexquiz.question.adapter.out.mapper.QuestionMapperImpl;
import com.example.indexquiz.question.adapter.out.mapper.QuestionOptionMapperImpl;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        GetQuestionPersistenceAdapter.class,
        QuestionMapperImpl.class,
        QuestionOptionMapperImpl.class
})
class GetQuestionPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private GetQuestionPersistenceAdapter getQuestionPersistenceAdapter;

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @Autowired
    private QuestionOptionJpaRepository questionOptionJpaRepository;

    @Nested
    class GetQuestionWithOptions {

        @Test
        void 하나의_질문과_연관된_선택지를_조회한다() {
            // given
            QuestionEntity savedQuestion = questionJpaRepository.save(
                    EntityFixture.getQuestionEntity(1));
            List<QuestionOptionEntity> savedOptions = questionOptionJpaRepository.saveAll(
                    EntityFixture.getQuestionOptionEntities(savedQuestion.getId(), 3));

            // when
            QuestionWithOptions actual = getQuestionPersistenceAdapter.getQuestionWithOptions(1L);

            // then
            Question actualQuestion = actual.getQuestion();
            List<QuestionOption> actualOptions = actual.getOptions().getQuestionOptions();
            assertAll(
                    () -> assertThat(actualQuestion.getId()).isEqualTo(savedQuestion.getId()),
                    () -> assertThat(actualOptions.stream().map(QuestionOption::getId).toList())
                            .containsExactlyElementsOf(savedOptions.stream()
                                    .map(QuestionOptionEntity::getId)
                                    .toList())

            );
        }
    }
}
