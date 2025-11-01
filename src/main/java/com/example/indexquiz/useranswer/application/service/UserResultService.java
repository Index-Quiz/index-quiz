package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.useranswer.application.port.in.SaveUserResultUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.application.port.in.mapper.UserResultDtoMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.application.port.out.SendUserResultMessagePort;
import com.example.indexquiz.useranswer.domain.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserResultService implements SaveUserResultUseCase {

    private final SendUserResultMessagePort sendUserResultMessagePort;

    private final SaveUserResultPort saveUserResultPort;

    private final UserResultDtoMapper userResultDtoMapper;


    @Override
    public SaveUserResultResponse saveUserResult(SaveUserResultRequest request) {
        UserResult userResult = new UserResult(request.questionSetName(), request.score());
        UserResult savedUserResult = saveUserResultPort.saveUserResult(userResult);
        sendUserResultMessagePort.sendUserResultMessage(savedUserResult);
        return userResultDtoMapper.mapToSaveUserResultResponse(savedUserResult);
    }
}
