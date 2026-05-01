package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.domain.ScoreCounts;

public interface GetScoreDistributionPort {

    ScoreCounts getScoreCounts(QuestionSet questionSet);
}
