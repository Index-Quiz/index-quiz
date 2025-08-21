package com.example.indexquiz.question.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionWithOptions {

    private final Question question;

    private final QuestionOptions options;

    public List<String> getOptionContents() {
        return options.getQuestionOptions().stream()
                .map(QuestionOption::getContent)
                .toList();
    }

    public QuestionWithOptions(Question question, List<QuestionOption> options) {
        this.question = question;
        this.options = new QuestionOptions(options);
    }
}
