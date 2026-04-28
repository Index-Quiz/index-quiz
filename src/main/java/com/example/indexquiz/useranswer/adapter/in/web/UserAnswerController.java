package com.example.indexquiz.useranswer.adapter.in.web;

import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserResultWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetQuestionSetAveragesWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.mapper.UserAnswerWebMapper;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.GetUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserResultUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import java.net.URI;
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

    private final UserAnswerWebMapper userAnswerWebMapper;

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
            @RequestBody SaveUserAnswerWebRequest request
    ) {
        SaveUserAnswerRequest saveUserAnswerRequest = userAnswerWebMapper.mapToSaveUserAnswerRequest(request);
        SaveUserAnswerResponse saveUserAnswerResponse = saveUserAnswerUseCase.saveUserAnswers(saveUserAnswerRequest);
        SaveUserAnswerWebResponse webResponse = userAnswerWebMapper.mapToSaveUserAnswerWebResponse(
                saveUserAnswerResponse);
        return ResponseEntity.ok(webResponse);
    }

    @PostMapping("/results")
    public ResponseEntity<SaveUserResultWebResponse> saveUserAnswerResults(
            @RequestBody SaveUserResultWebRequest request
    ) {
        SaveUserResultRequest saveUserResultRequest = userAnswerWebMapper.mapToSaveUserResultRequest(request);
        SaveUserResultResponse saveUserResultResponse = saveUserResultUseCase.saveUserResult(saveUserResultRequest);
        SaveUserResultWebResponse webResponse = userAnswerWebMapper.mapToSaveUserResultWebResponse(saveUserResultResponse);
        return ResponseEntity.created(URI.create("user-answers/" + webResponse.id()))
                .body(webResponse);
    }
}
