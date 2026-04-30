package com.example.indexquiz.common;

import com.example.indexquiz.learn.adapter.out.persistence.LearnMaterialEntity;
import com.example.indexquiz.question.adapter.out.persistence.QuestionEntity;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionSet;
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

    public static LearnMaterialEntity getLearnMaterialEntity(QuestionSet questionSet, int displayOrder) {
        return new LearnMaterialEntity(
                null,
                questionSet,
                "학습자료 제목" + displayOrder,
                "학습자료 설명" + displayOrder,
                "# 학습자료 내용" + displayOrder,
                displayOrder
        );
    }
}
