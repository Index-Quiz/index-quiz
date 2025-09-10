package com.example.indexquiz.solution.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.solution.adapter.out.mapper.SolutionMapper;
import com.example.indexquiz.solution.adapter.out.mapper.SolutionMapperImpl;
import com.example.indexquiz.solution.domain.Solution;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        GetSolutionPersistenceAdapter.class,
        SolutionMapperImpl.class
})
class GetSolutionPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private GetSolutionPersistenceAdapter getSolutionPersistenceAdapter;

    @Autowired
    private SolutionJpaRepository solutionJpaRepository;

    @Autowired
    private SolutionMapper solutionMapper;

    @Nested
    class GetSolutionByQuestionId {

        @Test
        void 질문_아이디로_해설을_가져올_수_있다() {
            long questionId =  1L;
            Solution solution = new Solution(null, questionId, "해설");
            solutionJpaRepository.save(solutionMapper.mapToSolutionEntity(solution));

            Solution foundSolution = getSolutionPersistenceAdapter.getByQuestionId(questionId);

            assertAll(
                    () -> assertThat(solution.getQuestionId()).isEqualTo(foundSolution.getQuestionId()),
                    () -> assertThat(solution.getDescription()).isEqualTo(foundSolution.getDescription())
            );
        }
    }
}
