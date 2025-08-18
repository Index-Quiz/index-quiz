package com.example.indexquiz.question.adapter.in.web;

import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionUseCase questionUseCase;

    @GetMapping("/questions/{questionId}")
    public GetQuestionResponse getQuestion(@PathVariable long questionId) {
        return questionUseCase.getQuestion(questionId);
    }
}
