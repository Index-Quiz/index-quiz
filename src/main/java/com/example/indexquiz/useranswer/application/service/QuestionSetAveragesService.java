package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionSetAveragesService implements GetQuestionSetAveragesUseCase {

    private final GetQuestionSetAveragesPort getQuestionSetAveragesPort;

    @Override
    @Transactional(readOnly = true)
    public GetQuestionSetAveragesResponse getQuestionSetAverages() {
        Map<QuestionSet, Double> averageMap = Arrays.stream(QuestionSet.values())
                .flatMap(questionSet -> getQuestionSetAveragesPort
                        .getAverageScore(questionSet, questionSet.getQuestionCount())
                        .map(avg -> new AbstractMap.SimpleEntry<>(questionSet, Math.round(avg * 10.0) / 10.0))
                        .stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new GetQuestionSetAveragesResponse(averageMap);
    }
}
