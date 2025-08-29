package com.example.indexquiz.solution.application.port.out;

import com.example.indexquiz.solution.domain.Solution;

public interface GetSolutionPort {

    Solution getByQuestionId(long questionId);
}
