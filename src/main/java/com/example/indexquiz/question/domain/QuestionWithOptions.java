package com.example.indexquiz.question.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionWithOptions {

    private final Question question;

    private final QuestionOptions options;

    public QuestionWithOptions(Question question, List<QuestionOption> options) {
        this(question, new QuestionOptions(options));
    }

    public List<QuestionOption> getOptions() {
        return options.getQuestionOptions();
    }

    public long getQuestionId() {
        return question.getId();
    }
}
