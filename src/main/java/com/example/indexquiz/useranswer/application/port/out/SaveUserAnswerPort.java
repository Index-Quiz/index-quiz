package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.UserAnswer;
import java.util.List;

public interface SaveUserAnswerPort {

    void saveUserAnswers(List<UserAnswer> userAnswers);
}
