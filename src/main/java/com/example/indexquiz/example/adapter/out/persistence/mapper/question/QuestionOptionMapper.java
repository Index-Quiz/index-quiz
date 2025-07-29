package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionOptionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.question.QuestionOption;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionOptionMapper {

    QuestionOptionEntity mapToEntity(QuestionOption questionOption);

    QuestionOption mapToDomain(QuestionOptionEntity questionOptionEntity);
}
