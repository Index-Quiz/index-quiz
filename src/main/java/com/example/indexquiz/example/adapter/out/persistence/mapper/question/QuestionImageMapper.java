package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionImageEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.question.QuestionImage;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionImageMapper {

    QuestionImageEntity mapToEntity(QuestionImage questionImage);

    QuestionImage mapToDomain(QuestionImageEntity questionImageEntity);
}
