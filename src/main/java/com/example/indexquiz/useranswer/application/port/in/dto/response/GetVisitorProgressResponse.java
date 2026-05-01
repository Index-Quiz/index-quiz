package com.example.indexquiz.useranswer.application.port.in.dto.response;

import com.example.indexquiz.useranswer.domain.SetBestScore;
import java.util.List;

public record GetVisitorProgressResponse(
        List<SetBestScore> completedSets,
        int progressPercentage
) {

}
