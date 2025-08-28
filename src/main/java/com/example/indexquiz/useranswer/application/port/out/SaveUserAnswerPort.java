package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;

public interface SaveUserAnswerPort {

    void saveUserAnswers(SaveUserAnswersCommand command);
}
