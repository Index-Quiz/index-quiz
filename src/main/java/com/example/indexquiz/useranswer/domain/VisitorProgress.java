package com.example.indexquiz.useranswer.domain;

import java.util.List;
import lombok.Getter;

@Getter
public class VisitorProgress {

    private static final int TRACKABLE_SET_COUNT = 8;

    private final List<SetBestScore> completedSets;

    public VisitorProgress(List<SetBestScore> completedSets) {
        this.completedSets = List.copyOf(completedSets);
    }

    public int progressPercentage() {
        return completedSets.size() * 100 / TRACKABLE_SET_COUNT;
    }
}
