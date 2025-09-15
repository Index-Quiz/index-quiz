package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface UserResultDtoMapper {

    @Mapping(source = "questionSet", target = "questionSetName")
    SaveUserResultResponse mapToSaveUserResultResponse(UserResult userResult);
}
