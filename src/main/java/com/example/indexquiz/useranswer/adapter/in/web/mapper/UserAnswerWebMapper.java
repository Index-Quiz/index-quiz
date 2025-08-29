package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerWebMapper {

    SaveUserAnswerRequest mapToSaveUserAnswerRequest(SaveUserAnswerWebRequest webRequest);

    SaveUserAnswerWebResponse mapToSaveUserAnswerWebResponse(SaveUserAnswerResponse applicationResponse);

    GetUserAnswerWebResponse mapToGetUserAnswerWebResponse(GetUserAnswerResponse applicationResponse);
}
