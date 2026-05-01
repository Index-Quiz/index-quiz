package com.example.indexquiz.learn.application.service;

import com.example.indexquiz.learn.application.port.in.LearnMaterialUseCase;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaries;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaryResponse;
import com.example.indexquiz.learn.application.port.in.mapper.LearnMaterialDtoMapper;
import com.example.indexquiz.learn.application.port.out.GetLearnMaterialPort;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LearnMaterialService implements LearnMaterialUseCase {

    private final GetLearnMaterialPort getLearnMaterialPort;

    private final LearnMaterialDtoMapper learnMaterialDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public GetLearnMaterialResponse getLearnMaterial(long id) {
        LearnMaterial material = getLearnMaterialPort.getById(id);
        return learnMaterialDtoMapper.mapToGetLearnMaterialResponse(material);
    }

    @Override
    @Transactional(readOnly = true)
    public GetLearnMaterialSummaries getLearnMaterialsBySet(QuestionSet questionSet) {
        return getLearnMaterialPort.getAllByQuestionSet(questionSet).stream()
                .map(learnMaterialDtoMapper::mapToGetLearnMaterialSummaryResponse)
                .collect(Collectors.collectingAndThen(Collectors.toList(), GetLearnMaterialSummaries::new));
    }
}
