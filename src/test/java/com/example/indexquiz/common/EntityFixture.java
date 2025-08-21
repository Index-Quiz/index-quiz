package com.example.indexquiz.common;

import com.example.indexquiz.question.adapter.out.persistence.QuestionEntity;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionType;
import java.util.List;
import java.util.stream.IntStream;

public class EntityFixture {

    public static QuestionEntity getQuestionEntity(int order) {
        return new QuestionEntity(null, QuestionType.SINGLE_CHOICE, "질문 내용" + order, order);
    }

    public static QuestionOptionEntity getQuestionOptionEntity(long questionId, int order) {
        return new QuestionOptionEntity(null, questionId, "선택지 내용" + order, order);
    }

    public static List<QuestionOptionEntity> getQuestionOptionEntities(long questionId, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(order -> getQuestionOptionEntity(questionId, order))
                .toList();
    }
}
