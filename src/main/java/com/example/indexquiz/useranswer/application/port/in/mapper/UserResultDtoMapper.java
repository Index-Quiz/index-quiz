package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.springframework.stereotype.Component;

@Component
public class UserResultDtoMapper {

    public SaveUserResultResponse mapToSaveUserResultResponse(UserResult userResult, ScoreDistribution distribution) {
        return new SaveUserResultResponse(
                userResult.getId(),
                userResult.getQuestionSet(),
                userResult.getScore(),
                distribution.distribution(),
                distribution.averageScore(),
                distribution.topPercentage()
        );
    }
}
