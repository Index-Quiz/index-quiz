package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAnswers {

    private final List<UserAnswer> values;

    public UserAnswers(long questionId, List<Long> userOptionIds) {
        String submitId = UUID.randomUUID().toString();
        this.values = userOptionIds.stream()
                .map(userOptionId -> new UserAnswer(questionId, userOptionId, submitId))
                .toList();
    }

    public boolean isCorrect(List<Long> answerOptions) {
        Set<Long> userAnswerOptions = values.stream()
                .map(UserAnswer::getOptionId)
                .collect(Collectors.toSet());
        return userAnswerOptions.size() == answerOptions.size()
                && userAnswerOptions.containsAll(answerOptions);
    }

    public List<Long> getUserOptions() {
        return values.stream()
                .map(UserAnswer::getOptionId)
                .toList();
    }

    public long getQuestionId() {
        Set<Long> questionIds = values.stream()
                .map(UserAnswer::getQuestionId)
                .collect(Collectors.toSet());

        if (questionIds.size() != 1) {
            throw new IndexQuizException(ErrorCode.INVALID_USER_ANSWERS);
        }
        return values.get(0).getQuestionId();
    }

    public String getSubmitId() {
        Set<String> submitIds = values
                .stream()
                .map(UserAnswer::getSubmitId)
                .collect(Collectors.toSet());

        if (submitIds.size() != 1) {
            throw new IndexQuizException(ErrorCode.INVALID_USER_ANSWERS);
        }
        return values.get(0).getSubmitId();
    }
}
