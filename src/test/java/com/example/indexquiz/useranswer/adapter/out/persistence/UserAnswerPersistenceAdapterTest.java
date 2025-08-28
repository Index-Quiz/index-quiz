package com.example.indexquiz.useranswer.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapperImpl;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        UserAnswerPersistenceAdapter.class,
        UserAnswerMapperImpl.class
})
class UserAnswerPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private UserAnswerPersistenceAdapter userAnswerPersistenceAdapter;

    @Autowired
    private UserAnswerJpaRepository userAnswerJpaRepository;

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
}
