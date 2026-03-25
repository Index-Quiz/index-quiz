package com.example.indexquiz.question.application.port.out;

import com.example.indexquiz.question.application.port.out.dto.DifficultQuestionResponses;

public interface GetDifficultQuestionPort {

    DifficultQuestionResponses findDifficultQuestions(long problemCount);
}
