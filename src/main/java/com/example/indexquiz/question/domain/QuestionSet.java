package com.example.indexquiz.question.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionSet {

    A(15),
    ;

    private final int questionCount;

}
