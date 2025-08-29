package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;

public interface GetUserAnswerUseCase {

    GetUserAnswerResponse getUserAnswer(GetUserAnswerRequest getUserAnswerRequest);
}
