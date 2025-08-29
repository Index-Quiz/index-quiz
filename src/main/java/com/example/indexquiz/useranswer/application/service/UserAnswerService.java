package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.mapper.UserAnswerDtoMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService implements SaveUserAnswerUseCase {

    private final SaveUserAnswerPort saveUserAnswerPort;

    private final GetQuestionPort getQuestionPort;

    private final UserAnswerDtoMapper userAnswerDtoMapper;

    @Override
    public SaveUserAnswerResponse saveUserAnswers(SaveUserAnswerRequest request) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(request.questionId());
        SaveUserAnswersCommand command = new SaveUserAnswersCommand(questionWithOptions, request.options());
        UserAnswers userAnswers = saveUserAnswerPort.saveUserAnswers(command);
        return userAnswerDtoMapper.mapToSaveUserAnswerResponse(userAnswers);
    }
}
