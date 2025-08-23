package com.example.indexquiz.question.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionOption;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionOptionMapper {

    QuestionOptionEntity mapToQuestionOptionEntity(QuestionOption questionOption);

    QuestionOption mapToQuestionOption(QuestionOptionEntity questionOptionEntity);
}
