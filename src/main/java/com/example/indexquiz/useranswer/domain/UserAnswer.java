package com.example.indexquiz.useranswer.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAnswer {

    private final Long id;

    private final long questionId;

    private final long optionId;

    private final long submitId;
}
