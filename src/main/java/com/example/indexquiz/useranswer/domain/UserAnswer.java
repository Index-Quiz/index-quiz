package com.example.indexquiz.useranswer.domain;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAnswer {

    private final Long id;

    private final long questionId;

    private final long optionId;

    private final String submitId;

    public UserAnswer(long questionId, long optionId) {
        this(null, questionId, optionId, UUID.randomUUID().toString());
    }
}
