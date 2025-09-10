package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;

public interface SaveUserAnswerUseCase {

    SaveUserAnswerResponse saveUserAnswers(SaveUserAnswerRequest request);
}
