package com.example.indexquiz.example.adapter.out.persistence.mapper.answer;

import com.example.indexquiz.example.adapter.out.persistence.entity.answer.AnswerEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.answer.Answer;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface AnswerMapper {

    AnswerEntity mapToAnswerEntity(Answer answer);

    Answer mapToAnswer(AnswerEntity answerEntity);
}
