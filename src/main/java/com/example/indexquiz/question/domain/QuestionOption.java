package com.example.indexquiz.question.domain;

import java.util.Comparator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionOption {

    private static final Comparator<QuestionOption> DEFAULT_COMPARATOR = Comparator
            .comparingLong(QuestionOption::getOrder);

    private final Long id;

    private final long questionId;

    private final String content;

    private final long order;

    public static Comparator<QuestionOption> getDefaultComparator() {
        return DEFAULT_COMPARATOR;
    }
}
