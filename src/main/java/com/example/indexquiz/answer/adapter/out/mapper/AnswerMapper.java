package com.example.indexquiz.answer.adapter.out.mapper;

import com.example.indexquiz.answer.adapter.out.persistence.AnswerEntity;
import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.answer.domain.Answer;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface AnswerMapper {

    AnswerEntity mapToAnswerEntity(Answer answer);

    Answer mapToAnswer(AnswerEntity answerEntity);
}
