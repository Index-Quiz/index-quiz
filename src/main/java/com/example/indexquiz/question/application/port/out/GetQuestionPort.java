package com.example.indexquiz.question.application.port.out;

import com.example.indexquiz.question.domain.QuestionWithOptions;

public interface GetQuestionPort {

    QuestionWithOptions getQuestionWithOptions(long questionId);
}
