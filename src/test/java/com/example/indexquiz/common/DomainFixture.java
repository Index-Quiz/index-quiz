package com.example.indexquiz.common;

import com.example.indexquiz.answer.domain.Answer;
import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainFixture {

    public static Question getQuestion(int order) {
        return new Question((long) order, QuestionType.SINGLE_CHOICE, "질문 내용" + order, order);
    }

    public static QuestionOption getQuestionOption(long questionId, int order) {
        return new QuestionOption((long) order, questionId, "선택지 내용" + order, order);
    }

    public static List<QuestionOption> getQuestionOptions(long questionId, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(order -> getQuestionOption(questionId, order))
                .toList();
    }

    public static Answers getAnswers(long questionId, List<Long> answerOptions) {
        return IntStream.range(0, answerOptions.size())
                .mapToObj(idx -> new Answer((long) (idx + 1), questionId, answerOptions.get(idx)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Answers::new));
    }
}
