package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.question.domain.QuestionSet;

public record QuestionSetAverage(
        QuestionSet questionSet,
        double averageScore
) {

}
