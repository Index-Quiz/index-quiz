package com.example.indexquiz.question.application.port.in.dto;

import com.example.indexquiz.common.config.cache.CacheWeighable;
import java.util.List;

public record GetQuestionResponses (
        List<GetQuestionResponse> questions
) implements CacheWeighable {

    @Override
    public int estimateSize() {
        return questions.size() * 4 * 1024 + 128; //문제 한개당 4KB
    }
}
