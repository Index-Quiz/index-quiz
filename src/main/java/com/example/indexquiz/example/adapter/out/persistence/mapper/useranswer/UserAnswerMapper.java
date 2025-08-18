package com.example.indexquiz.example.adapter.out.persistence.mapper.useranswer;

import com.example.indexquiz.example.adapter.out.persistence.entity.useranswer.UserAnswerEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.useranswer.UserAnswer;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerMapper {

    UserAnswerEntity mapToUserAnswerEntity(UserAnswer userAnswer);

    UserAnswer mapToUserAnswer(UserAnswerEntity userAnswerEntity);
}
