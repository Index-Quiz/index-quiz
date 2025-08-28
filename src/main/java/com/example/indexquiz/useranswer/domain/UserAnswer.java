package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.common.mapper.Default;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserAnswer {

    private final Long id;

    private final long questionId;

    private final long optionId;

    private final String submitId;

    public UserAnswer(long questionId, long optionId) {
        this(null, questionId, optionId, UUID.randomUUID().toString());
    }

    @Default
    public UserAnswer(Long id, long questionId, long optionId, String submitId) {
        this.id = id;
        this.questionId = questionId;
        this.optionId = optionId;
        this.submitId = submitId;
    }
}
