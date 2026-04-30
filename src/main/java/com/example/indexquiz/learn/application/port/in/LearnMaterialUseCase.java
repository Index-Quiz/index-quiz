package com.example.indexquiz.learn.application.port.in;

import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialListResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.question.domain.QuestionSet;

public interface LearnMaterialUseCase {

    GetLearnMaterialResponse getLearnMaterial(long id);

    GetLearnMaterialListResponse getLearnMaterialsBySet(QuestionSet questionSet);
}
