package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
import com.example.indexquiz.useranswer.domain.QuestionSetAverage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionSetAveragesService implements GetQuestionSetAveragesUseCase {

    private final GetQuestionSetAveragesPort getQuestionSetAveragesPort;

    @Override
    public GetQuestionSetAveragesResponse getQuestionSetAverages() {
        List<QuestionSetAverage> averages = getQuestionSetAveragesPort.getAverageScoresByQuestionSet();

        Map<QuestionSet, Double> averageMap = averages.stream()
                .collect(Collectors.toMap(
                        QuestionSetAverage::questionSet,
                        avg -> Math.round(avg.averageScore() * 10.0) / 10.0
                ));

        return new GetQuestionSetAveragesResponse(averageMap);
    }
}
