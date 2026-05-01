package com.example.indexquiz.useranswer.adapter.in.web;

import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserResultWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetQuestionSetAveragesWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetVisitorProgressWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.mapper.UserAnswerWebMapper;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.GetUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.GetVisitorProgressUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserResultUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-answers")
public class UserAnswerController {

    private final SaveUserAnswerUseCase saveUserAnswerUseCase;

    private final SaveUserResultUseCase saveUserResultUseCase;

    private final GetUserAnswerUseCase getUserAnswerUseCase;

    private final GetQuestionSetAveragesUseCase getQuestionSetAveragesUseCase;

    private final GetVisitorProgressUseCase getVisitorProgressUseCase;

    private final UserAnswerWebMapper userAnswerWebMapper;

    private final VisitorCookieResolver visitorCookieResolver;

    @GetMapping("/results/averages")
    public ResponseEntity<GetQuestionSetAveragesWebResponse> getQuestionSetAverages() {
        GetQuestionSetAveragesResponse response = getQuestionSetAveragesUseCase.getQuestionSetAverages();
        GetQuestionSetAveragesWebResponse webResponse = userAnswerWebMapper.mapToGetQuestionSetAveragesWebResponse(response);
        return ResponseEntity.ok(webResponse);
    }

    @GetMapping("/{submitId}")
    public ResponseEntity<GetUserAnswerWebResponse> getUserAnswers(
            @PathVariable(name = "submitId") String submitId
    ) {
        GetUserAnswerResponse userAnswers = getUserAnswerUseCase.getUserAnswer(new GetUserAnswerRequest(submitId));
        GetUserAnswerWebResponse webResponse = userAnswerWebMapper.mapToGetUserAnswerWebResponse(userAnswers);
        return ResponseEntity.ok(webResponse);
    }

    @PostMapping
    public ResponseEntity<SaveUserAnswerWebResponse> saveUserAnswer(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            @RequestBody SaveUserAnswerWebRequest request
    ) {
        String visitorId = visitorCookieResolver.resolve(httpRequest, httpResponse);
        SaveUserAnswerRequest saveUserAnswerRequest = userAnswerWebMapper.mapToSaveUserAnswerRequest(request, visitorId);
        SaveUserAnswerResponse saveUserAnswerResponse = saveUserAnswerUseCase.saveUserAnswers(saveUserAnswerRequest);
        SaveUserAnswerWebResponse webResponse = userAnswerWebMapper.mapToSaveUserAnswerWebResponse(
                saveUserAnswerResponse);
        return ResponseEntity.ok(webResponse);
    }

    @PostMapping("/results")
    public ResponseEntity<SaveUserResultWebResponse> saveUserAnswerResults(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            @RequestBody SaveUserResultWebRequest request
    ) {
        String visitorId = visitorCookieResolver.resolve(httpRequest, httpResponse);
        SaveUserResultRequest saveUserResultRequest = userAnswerWebMapper.mapToSaveUserResultRequest(request, visitorId);
        SaveUserResultResponse saveUserResultResponse = saveUserResultUseCase.saveUserResult(saveUserResultRequest);
        SaveUserResultWebResponse webResponse = userAnswerWebMapper.mapToSaveUserResultWebResponse(saveUserResultResponse);
        return ResponseEntity.created(URI.create("user-answers/" + webResponse.id()))
                .body(webResponse);
    }

    @GetMapping("/progress")
    public ResponseEntity<GetVisitorProgressWebResponse> getVisitorProgress(
            HttpServletRequest httpRequest
    ) {
        return visitorCookieResolver.findExisting(httpRequest)
                .map(visitorId -> {
                    GetVisitorProgressResponse response = getVisitorProgressUseCase.getProgress(visitorId);
                    GetVisitorProgressWebResponse webResponse = userAnswerWebMapper.mapToGetVisitorProgressWebResponse(response);
                    return ResponseEntity.ok(webResponse);
                })
                .orElseGet(() -> ResponseEntity.ok(new GetVisitorProgressWebResponse(Map.of(), 0)));
    }
}
