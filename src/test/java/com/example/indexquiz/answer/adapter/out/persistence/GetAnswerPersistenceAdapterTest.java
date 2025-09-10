package com.example.indexquiz.answer.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.answer.adapter.out.mapper.AnswerMapper;
import com.example.indexquiz.answer.adapter.out.mapper.AnswerMapperImpl;
import com.example.indexquiz.answer.domain.Answer;
import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.solution.domain.Solution;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        GetAnswerPersistenceAdapter.class,
        AnswerMapperImpl.class,
})
class GetAnswerPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private GetAnswerPersistenceAdapter getAnswerPersistenceAdapter;

    @Autowired
    private AnswerJpaRepository answerJpaRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Nested
    class GetAnswersByQuestionId {

        @Test
        void 질문_아이디로_정답들을_가져올_수_있다() {
            long questionId = 1L;
            Answers answers = new Answers(List.of(
                    new Answer(null, questionId, 1L),
                    new Answer(null, questionId, 2L)
                    ));
            List<AnswerEntity> answerEntities = answers.getValues()
                    .stream()
                    .map(answerMapper::mapToAnswerEntity)
                    .toList();
            answerJpaRepository.saveAll(answerEntities);

            Answers foundAnswers = getAnswerPersistenceAdapter.getByQuestionId(questionId);

            assertThat(foundAnswers.getAnswerOptions()).containsExactlyElementsOf(answers.getAnswerOptions());
        }
    }

}
