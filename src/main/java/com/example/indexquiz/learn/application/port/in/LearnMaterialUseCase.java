package com.example.indexquiz.learn.application.port.in;

import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaries;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnPageResponse;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.Optional;

public interface LearnMaterialUseCase {

    GetLearnMaterialResponse getLearnMaterial(long id);

    GetLearnMaterialSummaries getLearnMaterialsBySet(QuestionSet questionSet);

    GetLearnPageResponse getLearnPageData(QuestionSet questionSet, Optional<Long> id);
}
