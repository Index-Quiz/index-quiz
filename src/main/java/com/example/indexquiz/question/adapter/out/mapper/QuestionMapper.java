package com.example.indexquiz.question.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.adapter.out.persistence.QuestionEntity;
import com.example.indexquiz.question.domain.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface QuestionMapper {

    @Mapping(source = "order", target = "questionOrder")
    QuestionEntity mapToQuestionEntity(Question question);

    @Mapping(source = "questionOrder", target = "order")
    Question mapToQuestion(QuestionEntity questionEntity);
}
