package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.Map;

public record GetVisitorProgressWebResponse(
        Map<String, Integer> bestScore,
        int progressPercentage
) {

}
