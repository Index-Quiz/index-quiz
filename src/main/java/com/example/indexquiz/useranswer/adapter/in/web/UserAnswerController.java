package com.example.indexquiz.useranswer.adapter.in.web;

import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.mapper.UserAnswerWebMapper;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
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

    private final UserAnswerWebMapper userAnswerWebMapper;

    @GetMapping("/{submitId}")
    public ResponseEntity<GetUserAnswerWebResponse> getUserAnswers(
            @PathVariable(name = "submitId") String submitId
    ) {
        return null;
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
}
