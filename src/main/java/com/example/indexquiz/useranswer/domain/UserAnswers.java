package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAnswers {

    private final List<UserAnswer> userAnswers;

    public String getSubmitId() {
        if (userAnswers.isEmpty()) {
            throw new IndexQuizException(ErrorCode.USER_ANSWER_EMPTY);
        }
        return userAnswers.get(0).getSubmitId();
    }
}
