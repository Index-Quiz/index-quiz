package com.example.indexquiz.question.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

    private final Long id;

    private final QuestionType type;

    private final String content;

    private final long questionOrder;
}
