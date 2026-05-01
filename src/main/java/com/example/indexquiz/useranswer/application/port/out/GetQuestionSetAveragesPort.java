package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Optional;

public interface GetQuestionSetAveragesPort {

    Optional<Double> getAverageScore(QuestionSet questionSet, int maxScore);
}
