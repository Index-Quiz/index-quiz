package com.example.indexquiz.example.application.port.out;

import com.example.indexquiz.example.domain.question.QuestionWithOptions;

public interface GetQuestionPort {

    QuestionWithOptions getQuestionWithOptions(long questionId);
}
