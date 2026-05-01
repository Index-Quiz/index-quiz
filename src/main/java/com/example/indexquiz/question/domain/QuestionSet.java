package com.example.indexquiz.question.domain;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionSet {

    A(7),
    B(7),
    C(7),
    D(7),
    E(7),
    F(7),
    G(7),
    H(7),
    BEST_DIFFICULT(7),
    ;

    private static final List<QuestionSet> ALL_SETS = List.of(values());
    private static final int QUESTIONS_PER_SET = 7;

    private final int questionCount;

    public void validateScore(int score) {
        if (score < 0 || score > questionCount) {
            throw new IndexQuizException(ErrorCode.SCORE_OUT_OF_RANGE);
        }
    }

    public boolean isDifficult(QuestionSet questionSet) {
        return BEST_DIFFICULT == questionSet;
    }

    public static Optional<QuestionSet> findByQuestionOrder(long questionOrder) {
        int index = Math.toIntExact((questionOrder - 1) / QUESTIONS_PER_SET);
        if (index < 0 || index >= ALL_SETS.size()) {
            return Optional.empty();
        }
        QuestionSet questionSet = ALL_SETS.get(index);
        if (questionSet == BEST_DIFFICULT) {
            return Optional.empty();
        }
        return Optional.of(questionSet);
    }


}
