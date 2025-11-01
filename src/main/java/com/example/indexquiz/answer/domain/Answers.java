package com.example.indexquiz.answer.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answers {

    private final List<Answer> values;

    public List<Long> getAnswerOptions() {
        return values.stream()
                .map(Answer::getOptionId)
                .toList();
    }
}
