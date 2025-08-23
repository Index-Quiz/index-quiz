package com.example.indexquiz.question.domain;

import java.util.Comparator;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionOptions {

    private static final Comparator<QuestionOption> DEFAULT_COMPARATOR = Comparator
            .comparingLong(QuestionOption::getOptionOrder);

    private final List<QuestionOption> questionOptions;

    public QuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions.stream()
                .sorted(DEFAULT_COMPARATOR)
                .toList();
    }
}
