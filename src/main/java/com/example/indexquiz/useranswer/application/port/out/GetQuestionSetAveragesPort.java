package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.QuestionSetAverage;
import java.util.List;

public interface GetQuestionSetAveragesPort {

    List<QuestionSetAverage> getAverageScoresByQuestionSet();
}
