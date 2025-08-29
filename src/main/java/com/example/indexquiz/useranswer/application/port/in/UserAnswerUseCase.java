package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;

public interface UserAnswerUseCase {

    SaveUserAnswerResponse saveUserAnswers(SaveUserAnswerRequest request);
}
