package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;

public interface UserAnswerUseCase {

    void saveUserAnswers(long questionId, SaveUserAnswerRequest request);
}
