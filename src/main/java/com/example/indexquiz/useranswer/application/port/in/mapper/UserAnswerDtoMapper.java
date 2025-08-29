package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import org.mapstruct.Mapper;

//port dto <-> domain
@Mapper(config = MapperConfiguration.class)
public interface UserAnswerDtoMapper {

    SaveUserAnswerResponse mapToSaveUserAnswerResponse(UserAnswers userAnswers);
}
