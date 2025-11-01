package com.example.indexquiz.useranswer.application.port.out.dto;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveUserAnswersCommand {

    private final UserAnswers userAnswers;

    public SaveUserAnswersCommand(QuestionWithOptions questionWithOptions, List<Long> userOptionIds) {
        validateQuestionOptionsValid(questionWithOptions.getOptions(), userOptionIds);
        this.userAnswers = new UserAnswers(questionWithOptions.getQuestionId(), userOptionIds);
    }

    private void validateQuestionOptionsValid(List<QuestionOption> options, List<Long> userOptionIds) {
        Set<Long> optionIds = options.stream()
                .map(QuestionOption::getId)
                .collect(Collectors.toUnmodifiableSet());
        if (!optionIds.containsAll(userOptionIds)) {
            throw new IndexQuizException(ErrorCode.QUESTION_OPTION_INVALID);
        }
    }

    public List<UserAnswer> getUserAnswers() {
        return Collections.unmodifiableList(userAnswers.getValues());
    }
}
