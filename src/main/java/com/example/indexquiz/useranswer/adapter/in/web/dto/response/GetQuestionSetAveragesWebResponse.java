package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

import java.util.List;

public record GetQuestionSetAveragesWebResponse(
        List<QuestionSetAverageWebEntry> averages
) {

}
