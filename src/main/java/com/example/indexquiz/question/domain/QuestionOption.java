package com.example.indexquiz.question.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionOption {

    private final Long id;

    private final long questionId;

    private final String content;

    private final long order;
}
