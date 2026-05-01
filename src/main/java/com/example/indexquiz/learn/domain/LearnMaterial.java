package com.example.indexquiz.learn.domain;

import com.example.indexquiz.question.domain.QuestionSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LearnMaterial {

    private final Long id;

    private final QuestionSet questionSet;

    private final String title;

    private final String description;

    private final String content;

    private final int displayOrder;
}
