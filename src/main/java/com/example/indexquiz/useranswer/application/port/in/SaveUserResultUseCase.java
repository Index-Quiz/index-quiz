package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;

public interface SaveUserResultUseCase {

    SaveUserResultResponse saveUserResult(SaveUserResultRequest saveUserResultRequest);
}
