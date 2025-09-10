package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.answer.application.port.out.GetAnswerPort;
import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.solution.application.port.out.GetSolutionPort;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.application.port.in.GetUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.mapper.UserAnswerDtoMapper;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.GetUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService implements SaveUserAnswerUseCase, GetUserAnswerUseCase {

    private final SaveUserAnswerPort saveUserAnswerPort;

    private final GetUserAnswerPort getUserAnswerPort;

    private final GetAnswerPort getAnswerPort;

    private final GetSolutionPort getSolutionPort;

    private final GetQuestionPort getQuestionPort;

    private final UserAnswerDtoMapper userAnswerDtoMapper;

    @Override
    public GetUserAnswerResponse getUserAnswer(GetUserAnswerRequest getUserAnswerRequest) {
        UserAnswers userAnswers = getUserAnswerPort.getBySubmitId(getUserAnswerRequest.submitId());
        long questionId = userAnswers.getQuestionId();
        Answers answers = getAnswerPort.getByQuestionId(questionId);
        Solution solution = getSolutionPort.getByQuestionId(questionId);
        return userAnswerDtoMapper.mapToGetUserAnswerResponse(userAnswers, answers, solution);
    }

    @Override
    public SaveUserAnswerResponse saveUserAnswers(SaveUserAnswerRequest request) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(request.questionId());
        SaveUserAnswersCommand command = new SaveUserAnswersCommand(questionWithOptions, request.options());
        UserAnswers userAnswers = saveUserAnswerPort.saveUserAnswers(command);
        return userAnswerDtoMapper.mapToSaveUserAnswerResponse(userAnswers);
    }
}
