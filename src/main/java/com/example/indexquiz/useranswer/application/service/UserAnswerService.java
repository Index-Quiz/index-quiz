package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.in.UserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAnswerService implements UserAnswerUseCase {

    private final SaveUserAnswerPort saveUserAnswerPort;

    private final GetQuestionPort getQuestionPort;

    @Override
    @Transactional
    public void saveUserAnswers(long questionId, SaveUserAnswerRequest request) {
        QuestionWithOptions questionWithOptions = getQuestionPort.getQuestionWithOptions(questionId);
        validateQuestionOptionsValid(questionWithOptions.getOptions(), request.options());
        List<UserAnswer> userAnswers = questionWithOptions.getOptions().stream()
                .map(option -> UserAnswer.from(questionWithOptions.getQuestion(), option))
                .toList();
        saveUserAnswerPort.saveUserAnswers(userAnswers);
    }

    private void validateQuestionOptionsValid(List<QuestionOption> options, List<Long> targetOptionIds) {
        Set<Long> optionIds = options.stream()
                .map(QuestionOption::getId)
                .collect(Collectors.toUnmodifiableSet());
        if (!optionIds.containsAll(targetOptionIds)) {
            throw new IllegalStateException(); // TODO: change to IndexQuestionException when #21 merge.
        }
    }
}
