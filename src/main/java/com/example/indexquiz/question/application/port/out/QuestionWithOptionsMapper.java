package com.example.indexquiz.question.application.port.out;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface QuestionWithOptionsMapper {

    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.type", target = "type")
    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "options.questionOptions", target = "options")
    GetQuestionResponse mapToGetQuestionResponse(QuestionWithOptions questionWithOptions);
}
