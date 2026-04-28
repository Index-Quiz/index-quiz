package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;

public interface GetScoreDistributionPort {

    ScoreDistribution getScoreDistribution(QuestionSet questionSet, int userScore);
}
