package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.List;

public record GetVisitorProgressWebResponse(
        List<SetBestScoreWebEntry> completedSets,
        int progressPercentage
) {

    public static GetVisitorProgressWebResponse noneProgress() {
        return new GetVisitorProgressWebResponse(List.of(), 0);
    }
}
