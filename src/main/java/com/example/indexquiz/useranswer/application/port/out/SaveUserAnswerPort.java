package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswers;

public interface SaveUserAnswerPort {

    UserAnswers saveUserAnswers(SaveUserAnswersCommand command);
}
