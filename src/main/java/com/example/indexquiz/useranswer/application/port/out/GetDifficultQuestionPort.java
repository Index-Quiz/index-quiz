package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.application.port.out.dto.DifficultQuestionResponses;

public interface GetDifficultQuestionPort {

    DifficultQuestionResponses findDifficultQuestions(long problemCount);
}
