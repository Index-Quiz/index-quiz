package com.example.indexquiz.example.domain.question;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

    private final Long id;
    private final QuestionType type;
    private final String content;
    private final long order;
}
