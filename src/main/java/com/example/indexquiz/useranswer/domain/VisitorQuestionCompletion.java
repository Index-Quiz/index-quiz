package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.common.mapper.Default;
import com.example.indexquiz.question.domain.QuestionSet;
import lombok.Getter;

@Getter
public class VisitorQuestionCompletion {

    private final Long id;

    private final String visitorId;

    private final long questionId;

    private final QuestionSet questionSet;

    @Default
    public VisitorQuestionCompletion(Long id, String visitorId, long questionId, QuestionSet questionSet) {
        this.id = id;
        this.visitorId = visitorId;
        this.questionId = questionId;
        this.questionSet = questionSet;
    }

    public VisitorQuestionCompletion(String visitorId, long questionId, QuestionSet questionSet) {
        this(null, visitorId, questionId, questionSet);
    }
}
