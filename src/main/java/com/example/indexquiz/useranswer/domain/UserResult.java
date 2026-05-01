package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.common.mapper.Default;
import com.example.indexquiz.question.domain.QuestionSet;
import lombok.Getter;

@Getter
public class UserResult {

    private final Long id;

    private final QuestionSet questionSet;

    private final int score;

    private final String visitorId;

    @Default
    public UserResult(Long id, QuestionSet questionSet, int score, String visitorId) {
        this.id = id;
        this.questionSet = questionSet;
        this.score = score;
        this.visitorId = visitorId;
    }

    public UserResult(QuestionSet questionSet, int score, String visitorId) {
        this(null, questionSet, score, visitorId);
    }
}
