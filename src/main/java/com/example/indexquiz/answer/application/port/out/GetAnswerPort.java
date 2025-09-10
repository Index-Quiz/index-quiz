package com.example.indexquiz.answer.application.port.out;

import com.example.indexquiz.answer.domain.Answers;

public interface GetAnswerPort {

    Answers getByQuestionId(long questionId);
}
