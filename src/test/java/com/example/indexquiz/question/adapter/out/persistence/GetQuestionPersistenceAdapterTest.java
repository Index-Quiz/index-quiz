package com.example.indexquiz.question.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.common.EntityFixture;
import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
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
    class GetQuestionWithOptionsByOrder {

        @Test
        void 질문순서를기반으로_하나의_질문과_연관된_선택지를_조회한다() {
            // given
            QuestionEntity savedQuestion = questionJpaRepository.save(
                    EntityFixture.getQuestionEntity(1));
            List<QuestionOptionEntity> savedOptions = questionOptionJpaRepository.saveAll(
                    EntityFixture.getQuestionOptionEntities(savedQuestion.getId(), 3));

            // when
            QuestionWithOptions actual = getQuestionPersistenceAdapter.getQuestionWithOptionsByOrder(1L);

            // then
            Question actualQuestion = actual.getQuestion();
            List<QuestionOption> actualOptions = actual.getOptions();
            assertAll(
                    () -> assertThat(actualQuestion.getId()).isEqualTo(savedQuestion.getId()),
                    () -> assertThat(actualOptions.stream().map(QuestionOption::getId).toList())
                            .containsExactlyElementsOf(savedOptions.stream()
                                    .map(QuestionOptionEntity::getId)
                                    .toList())

            );
        }

        @Test
        void 존재하지_않는_질문의_경우_예외를_반환한다() {
            // when & then
            assertThatThrownBy(() -> getQuestionPersistenceAdapter.getQuestionWithOptionsByOrder(1L))
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.QUESTION_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class GetQuestionWithOptionsById {

        @Test
        void 질문_아이디를_기반으로_하나의_질문과_연관된_선택지를_조회한다() {
            // given
            QuestionEntity savedQuestion = questionJpaRepository.save(
                    EntityFixture.getQuestionEntity(3));
            List<QuestionOptionEntity> savedOptions = questionOptionJpaRepository.saveAll(
                    EntityFixture.getQuestionOptionEntities(savedQuestion.getId(), 3));

            // when
            QuestionWithOptions actual = getQuestionPersistenceAdapter.getQuestionWithOptionsById(savedQuestion.getId());

            // then
            Question actualQuestion = actual.getQuestion();
            List<QuestionOption> actualOptions = actual.getOptions();
            assertAll(
                    () -> assertThat(actualQuestion.getId()).isEqualTo(savedQuestion.getId()),
                    () -> assertThat(actualOptions.stream().map(QuestionOption::getId).toList())
                            .containsExactlyElementsOf(savedOptions.stream()
                                    .map(QuestionOptionEntity::getId)
                                    .toList())

            );
        }

        @Test
        void 존재하지_않는_질문의_경우_예외를_반환한다() {
            // when & then
            assertThatThrownBy(() -> getQuestionPersistenceAdapter.getQuestionWithOptionsById(1L))
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.QUESTION_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class GetAllQuestionWithOptions {

        @Test
        void 질문_아이디에_포함되는_질문과_연관된_선택지를_조회한다() {
            // given
            QuestionEntity savedQuestion1 = questionJpaRepository.save(
                    EntityFixture.getQuestionEntity(1));
            QuestionEntity savedQuestion2 = questionJpaRepository.save(
                    EntityFixture.getQuestionEntity(2));
            List<QuestionOptionEntity> savedOptions1 = questionOptionJpaRepository.saveAll(
                    EntityFixture.getQuestionOptionEntities(savedQuestion1.getId(), 3));
            List<QuestionOptionEntity> savedOptions2 = questionOptionJpaRepository.saveAll(
                    EntityFixture.getQuestionOptionEntities(savedQuestion2.getId(), 2));

            // when
            List<QuestionWithOptions> actual = getQuestionPersistenceAdapter.getAllQuestionWithOptions(
                    List.of(savedQuestion1.getId(), savedQuestion2.getId())
            );

            // then
            Question actualQuestion1 = actual.get(0).getQuestion();
            Question actualQuestion2 = actual.get(1).getQuestion();
            List<QuestionOption> actualOptions1 = actual.get(0).getOptions();
            List<QuestionOption> actualOptions2 = actual.get(1).getOptions();
            assertAll(
                    () -> assertThat(actualQuestion1.getId()).isEqualTo(savedQuestion1.getId()),
                    () -> assertThat(actualQuestion2.getId()).isEqualTo(savedQuestion2.getId()),
                    () -> assertThat(actualOptions1.stream().map(QuestionOption::getId).toList())
                            .containsExactlyElementsOf(savedOptions1.stream()
                                    .map(QuestionOptionEntity::getId)
                                    .toList()),
                    () -> assertThat(actualOptions2.stream().map(QuestionOption::getId).toList())
                            .containsExactlyElementsOf(savedOptions2.stream()
                                    .map(QuestionOptionEntity::getId)
                                    .toList())
            );
        }
    }
}
