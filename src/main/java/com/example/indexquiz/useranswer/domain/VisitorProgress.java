package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class VisitorProgress {

    private static final int TRACKABLE_SET_COUNT = 8;

    private final Map<QuestionSet, Integer> completedSetBestScore;

    public VisitorProgress(Map<QuestionSet, Integer> completedSetBestScore) {
        this.completedSetBestScore = Collections.unmodifiableMap(completedSetBestScore);
    }

    public int completedCount() {
        return completedSetBestScore.size();
    }

    public int progressPercentage() {
        return completedCount() * 100 / TRACKABLE_SET_COUNT;
    }
}
