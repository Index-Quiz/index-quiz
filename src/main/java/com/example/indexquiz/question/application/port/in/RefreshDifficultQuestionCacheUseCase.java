package com.example.indexquiz.question.application.port.in;

import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;

public interface RefreshDifficultQuestionCacheUseCase {

    GetQuestionResponses getDifficultQuestion();
}
