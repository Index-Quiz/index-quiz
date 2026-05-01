package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.question.domain.QuestionSet;

public record SetBestScore(
        QuestionSet questionSet,
        int bestScore
) {

}
