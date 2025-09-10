package com.example.indexquiz.solution.adapter.out.persistence;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.solution.adapter.out.mapper.SolutionMapper;
import com.example.indexquiz.solution.application.port.out.GetSolutionPort;
import com.example.indexquiz.solution.domain.Solution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetSolutionPersistenceAdapter implements GetSolutionPort {

    private final SolutionJpaRepository solutionJpaRepository;

    private final SolutionMapper solutionMapper;

    @Override
    public Solution getByQuestionId(long questionId) {
        return solutionJpaRepository.findByQuestionId(questionId)
                .map(solutionMapper::mapToSolution)
                .orElseThrow(() -> new IndexQuizException(ErrorCode.SOLUTION_NOT_FOUND));
    }
}
