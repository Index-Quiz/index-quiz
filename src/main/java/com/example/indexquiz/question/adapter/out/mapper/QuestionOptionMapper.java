package com.example.indexquiz.question.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface QuestionOptionMapper {

    @Mapping(source = "order", target = "optionOrder")
    QuestionOptionEntity mapToQuestionOptionEntity(QuestionOption questionOption);

    @Mapping(source = "optionOrder", target = "order")
    QuestionOption mapToQuestionOption(QuestionOptionEntity questionOptionEntity);
}
