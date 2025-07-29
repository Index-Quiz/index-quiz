package com.example.indexquiz.example.domain.answer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {

    private final Long id;
    private final long questionId;
    private final long optionId;
}
