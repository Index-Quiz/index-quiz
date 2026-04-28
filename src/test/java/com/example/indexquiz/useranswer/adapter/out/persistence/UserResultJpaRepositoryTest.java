package com.example.indexquiz.useranswer.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserResultJpaRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserResultJpaRepository userResultJpaRepository;

    @Nested
    class FindAverageByQuestionSetAndMaxScore {

        @Test
        void 데이터가_없으면_빈_Optional을_반환한다() {
            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void 유효_점수만으로_평균을_계산한다() {
            // given
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 3));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 5));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 7));

            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(5.0);
        }

        @Test
        void maxScore를_초과하는_이상치는_평균에서_제외한다() {
            // given
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 3));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 5));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 15));

            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(4.0);
        }

        @Test
        void 다른_문제세트의_데이터는_포함하지_않는다() {
            // given
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 4));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 6));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.B, 2));

            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(5.0);
        }

        @Test
        void 모든_점수가_이상치이면_빈_Optional을_반환한다() {
            // given
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 10));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 15));

            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void 점수_0은_유효한_점수로_포함한다() {
            // given
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 0));
            userResultJpaRepository.save(new UserResultEntity(null, QuestionSet.A, 4));

            // when
            Optional<Double> result = userResultJpaRepository
                    .findAverageByQuestionSetAndMaxScore(QuestionSet.A, 7);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(2.0);
        }
    }
}
