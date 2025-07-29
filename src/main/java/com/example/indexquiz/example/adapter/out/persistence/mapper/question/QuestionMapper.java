package com.example.indexquiz.example.adapter.out.persistence.mapper.question;

import com.example.indexquiz.example.adapter.out.persistence.entity.question.QuestionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.question.Question;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionMapper {

    QuestionEntity mapToEntity(Question question);

    Question mapToDomain(QuestionEntity questionEntity);
}
