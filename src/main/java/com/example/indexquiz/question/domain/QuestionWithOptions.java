package com.example.indexquiz.question.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionWithOptions {

    private final Question question;

    private final List<QuestionOption> options;

    public List<String> getOptionContents() {
        return options.stream()
                .map(QuestionOption::getContent)
                .toList();
    }
}
