package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResult {

    private final Long id;

    private final QuestionSet questionSet;

    private final int score;

    private final String submitId;

    public UserResult(QuestionSet questionSet, int score) {
        this.id = null;
        this.questionSet = questionSet;
        this.score = score;
        this.submitId = UUID.randomUUID().toString();
    }
}
