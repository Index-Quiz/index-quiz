package com.example.indexquiz.useranswer.adapter.in.web;

import com.example.indexquiz.useranswer.application.port.in.UserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAnswerController {

    private final UserAnswerUseCase userAnswerUseCase;

    @PostMapping("/api/questions/{questionId}/userAnswers")
    public void saveUserAnswer(@PathVariable long questionId, @RequestBody SaveUserAnswerRequest request) {
        userAnswerUseCase.saveUserAnswers(questionId, request);
    }
}
