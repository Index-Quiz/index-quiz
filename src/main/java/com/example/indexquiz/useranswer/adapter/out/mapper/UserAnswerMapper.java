package com.example.indexquiz.useranswer.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.adapter.out.persistence.UserAnswerEntity;
import com.example.indexquiz.useranswer.domain.UserAnswer;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerMapper {

    UserAnswerEntity mapToUserAnswerEntity(UserAnswer userAnswer);

    UserAnswer mapToUserAnswer(UserAnswerEntity userAnswerEntity);
}
