package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.UserAnswers;

public interface GetUserAnswerPort {

    UserAnswers getBySubmitId(String submitId);
}
