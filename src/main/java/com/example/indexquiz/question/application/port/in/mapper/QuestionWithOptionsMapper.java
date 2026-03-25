package com.example.indexquiz.question.application.port.in.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface QuestionWithOptionsMapper {

    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.type", target = "type")
    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "options", target = "options")
    GetQuestionResponse mapToGetQuestionResponse(QuestionWithOptions questionWithOptions);

    default GetQuestionResponses mapToGetQuestionResponses(
            List<QuestionWithOptions> questionWithOptions
    ) {
        return questionWithOptions.stream()
                .map(this::mapToGetQuestionResponse)
                .collect(Collectors.collectingAndThen(Collectors.toList(), GetQuestionResponses::new));
    }
}
