package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.useranswer.application.port.in.SaveUserResultUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.application.port.in.mapper.UserResultDtoMapper;
import com.example.indexquiz.useranswer.application.port.out.GetScoreDistributionPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.application.port.out.SendUserResultMessagePort;
import com.example.indexquiz.useranswer.domain.ScoreCounts;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;
import com.example.indexquiz.useranswer.domain.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserResultService implements SaveUserResultUseCase {

    private final SendUserResultMessagePort sendUserResultMessagePort;

    private final SaveUserResultPort saveUserResultPort;

    private final GetScoreDistributionPort getScoreDistributionPort;

    private final UserResultDtoMapper userResultDtoMapper;


    @Override
    @Transactional
    public SaveUserResultResponse saveUserResult(SaveUserResultRequest request) {
        request.questionSetName().validateScore(request.score());
        UserResult userResult = new UserResult(request.questionSetName(), request.score());
        UserResult savedUserResult = saveUserResultPort.saveUserResult(userResult);
        sendUserResultMessagePort.sendUserResultMessage(savedUserResult);

        ScoreCounts scoreCounts = getScoreDistributionPort.getScoreCounts(request.questionSetName());
        ScoreDistribution distribution = scoreCounts.toDistribution(request.score());

        return userResultDtoMapper.mapToSaveUserResultResponse(savedUserResult, distribution);
    }
}
