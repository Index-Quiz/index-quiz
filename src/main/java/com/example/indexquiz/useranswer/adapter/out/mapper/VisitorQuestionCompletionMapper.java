package com.example.indexquiz.useranswer.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.adapter.out.persistence.VisitorQuestionCompletionEntity;
import com.example.indexquiz.useranswer.domain.VisitorQuestionCompletion;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface VisitorQuestionCompletionMapper {

    VisitorQuestionCompletionEntity mapToEntity(VisitorQuestionCompletion domain);

    VisitorQuestionCompletion mapToDomain(VisitorQuestionCompletionEntity entity);
}
