package com.example.indexquiz.learn.application.port.out;

import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;

public interface GetLearnMaterialPort {

    LearnMaterial getById(long id);

    List<LearnMaterial> getAllByQuestionSet(QuestionSet questionSet);
}
