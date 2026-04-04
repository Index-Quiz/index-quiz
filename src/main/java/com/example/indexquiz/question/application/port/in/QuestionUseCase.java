package com.example.indexquiz.question.application.port.in;

import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.domain.QuestionSet;

public interface QuestionUseCase {

    GetQuestionResponse getQuestion(long questionOrder);

    GetQuestionResponses getAllQuestions(QuestionSet type);
}
