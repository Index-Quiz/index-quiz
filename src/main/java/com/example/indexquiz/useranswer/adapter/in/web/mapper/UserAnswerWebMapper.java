package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserResultWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetQuestionSetAveragesWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.QuestionSetAverageWebEntry;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetVisitorProgressWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SetBestScoreWebEntry;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserAnswerWebMapper {

    default SaveUserAnswerRequest mapToSaveUserAnswerRequest(SaveUserAnswerWebRequest webRequest, String visitorId) {
        return new SaveUserAnswerRequest(webRequest.questionId(), webRequest.options(), visitorId);
    }

    SaveUserAnswerWebResponse mapToSaveUserAnswerWebResponse(SaveUserAnswerResponse applicationResponse);

    default SaveUserResultRequest mapToSaveUserResultRequest(SaveUserResultWebRequest webRequest, String visitorId) {
        return new SaveUserResultRequest(
                QuestionSet.valueOf(webRequest.questionSetName()),
                webRequest.score(),
                visitorId
        );
    }

    SaveUserResultWebResponse mapToSaveUserResultWebResponse(SaveUserResultResponse applicationResponse);

    GetUserAnswerWebResponse mapToGetUserAnswerWebResponse(GetUserAnswerResponse applicationResponse);

    default GetQuestionSetAveragesWebResponse mapToGetQuestionSetAveragesWebResponse(GetQuestionSetAveragesResponse response) {
        List<QuestionSetAverageWebEntry> webEntries = response.averages().stream()
                .map(avg -> new QuestionSetAverageWebEntry(avg.questionSet().name(), avg.average()))
                .toList();
        return new GetQuestionSetAveragesWebResponse(webEntries);
    }

    default GetVisitorProgressWebResponse mapToGetVisitorProgressWebResponse(GetVisitorProgressResponse response) {
        List<SetBestScoreWebEntry> webEntries = response.completedSets().stream()
                .map(s -> new SetBestScoreWebEntry(s.questionSet().name(), s.bestScore()))
                .toList();
        return new GetVisitorProgressWebResponse(webEntries, response.progressPercentage());
    }
}
