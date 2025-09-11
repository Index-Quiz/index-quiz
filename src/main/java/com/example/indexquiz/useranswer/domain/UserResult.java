package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.common.mapper.Default;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserResult {

    private final Long id;

    private final QuestionSet questionSet;

    private final int score;

    private final String submitId;

    @Default
    public UserResult(Long id, QuestionSet questionSet, int score, String submitId) {
        this.id = id;
        this.questionSet = questionSet;
        this.score = score;
        this.submitId = submitId;
    }

    public UserResult(QuestionSet questionSet, int score) {
        this(null, questionSet, score, UUID.randomUUID().toString());
    }
}
