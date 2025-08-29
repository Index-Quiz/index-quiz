package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerWebMapper {

    SaveUserAnswerWebResponse mapToSaveUserAnswerWebResponse(SaveUserAnswerResponse applicationResponse);
}
