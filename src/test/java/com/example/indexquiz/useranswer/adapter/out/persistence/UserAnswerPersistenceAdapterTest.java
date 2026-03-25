package com.example.indexquiz.useranswer.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.answer.adapter.out.persistence.AnswerEntity;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapper;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapperImpl;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserResultMapperImpl;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import com.example.indexquiz.useranswer.domain.UserResult;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        UserAnswerPersistenceAdapter.class,
        UserAnswerMapperImpl.class,
        UserResultMapperImpl.class
})
class UserAnswerPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private UserAnswerPersistenceAdapter userAnswerPersistenceAdapter;

    @Autowired
    private UserAnswerJpaRepository userAnswerJpaRepository;

    @Autowired
    private UserResultJpaRepository userResultJpaRepository;

    @Autowired
    private UserAnswerMapper userAnswerMapper;

    @Nested
    class SaveUserAnswers {

        @Test
        void 사용자의_답변을_저장할_수_있다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);

            // when
            SaveUserAnswersCommand command = new SaveUserAnswersCommand(questionWithOptions, List.of(1L, 2L));
            userAnswerPersistenceAdapter.saveUserAnswers(command);

            // then
            List<UserAnswerEntity> savedUserAnswers = userAnswerJpaRepository.findAll();
            assertAll(
                    () -> assertThat(savedUserAnswers).hasSize(2),
                    () -> assertThat(savedUserAnswers.stream().map(UserAnswerEntity::getQuestionId))
                            .containsExactly(1L, 1L),
                    () -> assertThat(savedUserAnswers.stream().map(UserAnswerEntity::getOptionId))
                            .containsExactly(1L, 2L)

            );
        }
    }

    @Nested
    class GetUserAnswers {

        @Test
        void 사용자의_답변을_가져올_수_있다() {
            // given
            String submitId = UUID.randomUUID().toString();
            UserAnswers userAnswers = new UserAnswers(List.of(
                    new UserAnswer(1L, 1L, submitId),
                    new UserAnswer(1L, 2L, submitId)
            ));

            List<UserAnswerEntity> userAnswerEntities = userAnswers.getValues()
                    .stream()
                    .map(userAnswerMapper::mapToUserAnswerEntity)
                    .toList();
            userAnswerJpaRepository.saveAll(userAnswerEntities);

            // when
            UserAnswers foundUserAnswers = userAnswerPersistenceAdapter.getBySubmitId(submitId);

            // then
            assertAll(
                    () -> assertThat(foundUserAnswers.getQuestionId()).isEqualTo(userAnswers.getQuestionId()),
                    () -> assertThat(foundUserAnswers.getUserOptions()).isEqualTo(userAnswers.getUserOptions())
            );
        }
    }

    @Nested
    class SaveUserResult {

        @Test
        void 사용자의_성적을_저장할_수_있다() {
            // given
            UserResult userResult = new UserResult(QuestionSet.A, 10);

            // when
            UserResult saveUserResult = userAnswerPersistenceAdapter.saveUserResult(userResult);

            // then
            UserResultEntity userResultEntity = userResultJpaRepository.findById(saveUserResult.getId()).get();
            assertAll(
                    () -> assertThat(userResultEntity.getId()).isEqualTo(saveUserResult.getId()),
                    () -> assertThat(userResultEntity.getQuestionSet()).isEqualTo(saveUserResult.getQuestionSet()),
                    () -> assertThat(userResultEntity.getScore()).isEqualTo(saveUserResult.getScore())
            );
        }
    }
}
