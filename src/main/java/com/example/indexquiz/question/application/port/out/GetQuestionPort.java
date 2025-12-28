package com.example.indexquiz.question.application.port.out;

import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;

public interface GetQuestionPort {

    QuestionWithOptions getQuestionWithOptions(long questionId);

    List<QuestionWithOptions> getAllQuestionWithOptions(List<Long> questionIds);
}
