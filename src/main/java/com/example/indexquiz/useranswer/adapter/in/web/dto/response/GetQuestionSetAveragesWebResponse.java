package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.Map;

public record GetQuestionSetAveragesWebResponse(
        Map<String, Double> averages
) {

}
