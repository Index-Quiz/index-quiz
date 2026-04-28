package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
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
                .collect(Collectors.toMap(
                        questionSet -> questionSet,
                        questionSet -> getQuestionSetAveragesPort
                                .getAverageScore(questionSet, questionSet.getQuestionCount())
                                .map(avg -> Math.round(avg * 10.0) / 10.0)
                                .orElse(0.0)
                ));

        return new GetQuestionSetAveragesResponse(averageMap);
    }
}
