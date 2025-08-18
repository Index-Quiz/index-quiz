package com.example.indexquiz.question.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.adapter.out.persistence.QuestionEntity;
import com.example.indexquiz.question.domain.Question;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionMapper {

    QuestionEntity mapToQuestionEntity(Question question);

    Question mapToQuestion(QuestionEntity questionEntity);
}
