package com.example.indexquiz.question.application.service;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.domain.QuestionOptions;
import com.example.indexquiz.useranswer.application.port.out.GetDifficultQuestionPort;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.application.port.in.mapper.QuestionWithOptionsMapper;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.out.dto.DifficultQuestionResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {

    private static final long DIFFICULT_PROBLEM_COUNT = 7;

    private final GetQuestionPort getQuestionPort;

    private final GetDifficultQuestionPort getDifficultQuestionPort;

    private final QuestionWithOptionsMapper questionWithOptionsMapper;

    @Override
    public GetQuestionResponse getQuestion(long questionId) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(questionId);
        return questionWithOptionsMapper.mapToGetQuestionResponse(questionWithOptions);
    }

    @Override
    public GetQuestionResponses getAllQuestions(QuestionSet type) {
        if (!type.isDifficult(type)) {
            throw new IndexQuizException(ErrorCode.QUESTION_SET_BAD_REQUEST);
        }
        DifficultQuestionResponses difficultQuestions = getDifficultQuestionPort.findDifficultQuestions(DIFFICULT_PROBLEM_COUNT);
        List<QuestionWithOptions> allQuestionWithOptions = getQuestionPort.getAllQuestionWithOptions(difficultQuestions.questionIds());
        return questionWithOptionsMapper.mapToGetQuestionResponses(allQuestionWithOptions);
    }
}
