package com.example.indexquiz.learn.adapter.out.persistence;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.learn.adapter.out.mapper.LearnMaterialMapper;
import com.example.indexquiz.learn.application.port.out.GetLearnMaterialPort;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetLearnMaterialPersistenceAdapter implements GetLearnMaterialPort {

    private final LearnMaterialJpaRepository learnMaterialJpaRepository;

    private final LearnMaterialMapper learnMaterialMapper;

    @Override
    public LearnMaterial getById(long id) {
        return learnMaterialJpaRepository.findById(id)
                .map(learnMaterialMapper::mapToLearnMaterial)
                .orElseThrow(() -> new IndexQuizException(ErrorCode.LEARN_MATERIAL_NOT_FOUND));
    }

    @Override
    public List<LearnMaterial> getAllByQuestionSet(QuestionSet questionSet) {
        return learnMaterialJpaRepository.findAllByQuestionSetOrderByDisplayOrder(questionSet)
                .stream()
                .map(learnMaterialMapper::mapToLearnMaterial)
                .toList();
    }
}
