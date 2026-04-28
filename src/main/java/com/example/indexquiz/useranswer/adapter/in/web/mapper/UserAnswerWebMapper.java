package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserResultWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetQuestionSetAveragesWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerWebMapper {

    SaveUserAnswerRequest mapToSaveUserAnswerRequest(SaveUserAnswerWebRequest webRequest);

    SaveUserAnswerWebResponse mapToSaveUserAnswerWebResponse(SaveUserAnswerResponse applicationResponse);

    SaveUserResultRequest mapToSaveUserResultRequest(SaveUserResultWebRequest webRequest);

    SaveUserResultWebResponse mapToSaveUserResultWebResponse(SaveUserResultResponse applicationResponse);

    GetUserAnswerWebResponse mapToGetUserAnswerWebResponse(GetUserAnswerResponse applicationResponse);

    default GetQuestionSetAveragesWebResponse mapToGetQuestionSetAveragesWebResponse(GetQuestionSetAveragesResponse response) {
        Map<String, Double> stringKeyMap = response.averages().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));
        return new GetQuestionSetAveragesWebResponse(stringKeyMap);
    }
}
