package com.example.indexquiz.useranswer.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.question.domain.QuestionSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VisitorQuestionCompletionJpaRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private VisitorQuestionCompletionJpaRepository repository;

    @Nested
    class ExistsByVisitorIdAndQuestionId {

        @Test
        void 존재하는_기록이면_true를_반환한다() {
            repository.save(new VisitorQuestionCompletionEntity(null, "visitor-1", 1L, QuestionSet.A));

            assertThat(repository.existsByVisitorIdAndQuestionId("visitor-1", 1L)).isTrue();
        }

        @Test
        void 존재하지_않는_기록이면_false를_반환한다() {
            assertThat(repository.existsByVisitorIdAndQuestionId("visitor-1", 1L)).isFalse();
        }
    }

    @Nested
    class CountByVisitorIdAndQuestionSet {

        @Test
        void 방문자의_특정_세트_완료_문제_수를_반환한다() {
            repository.save(new VisitorQuestionCompletionEntity(null, "visitor-1", 1L, QuestionSet.A));
            repository.save(new VisitorQuestionCompletionEntity(null, "visitor-1", 2L, QuestionSet.A));
            repository.save(new VisitorQuestionCompletionEntity(null, "visitor-1", 3L, QuestionSet.A));
            repository.save(new VisitorQuestionCompletionEntity(null, "visitor-1", 8L, QuestionSet.B));

            long count = repository.countByVisitorIdAndQuestionSet("visitor-1", QuestionSet.A);

            assertThat(count).isEqualTo(3);
        }

        @Test
        void 기록이_없으면_0을_반환한다() {
            long count = repository.countByVisitorIdAndQuestionSet("visitor-1", QuestionSet.A);

            assertThat(count).isEqualTo(0);
        }
    }
}
