package com.example.indexquiz.question.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionSet {

    A(7),
    B(7),
    C(7),
    D(7),
    E(7),
    F(7),
    G(7),
    H(7),
    BEST_DIFFICULT(7),
    ;

    private final int questionCount;

    public boolean isDifficult(QuestionSet questionSet) {
        return BEST_DIFFICULT == questionSet;
    }
}
