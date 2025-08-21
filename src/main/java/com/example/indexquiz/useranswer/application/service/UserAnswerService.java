package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.in.UserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService implements UserAnswerUseCase {

    private final SaveUserAnswerPort saveUserAnswerPort;

    private final GetQuestionPort getQuestionPort;

    @Override
    public void saveUserAnswers(long questionId, SaveUserAnswerRequest request) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(questionId);
        SaveUserAnswersCommand command = new SaveUserAnswersCommand(questionWithOptions, request.options());
        saveUserAnswerPort.saveUserAnswers(command);
    }
}
