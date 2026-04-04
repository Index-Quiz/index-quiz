package com.example.indexquiz.question.adapter.in.web;

import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.domain.QuestionSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionUseCase questionUseCase;

    @GetMapping("/{questionId}")
    public GetQuestionResponse getQuestion(@PathVariable(name = "questionId") long questionId) {
        return questionUseCase.getQuestion(questionId);
    }

    @GetMapping
    public GetQuestionResponses getAllQuestions(@RequestParam(value = "type") String type) {
        return questionUseCase.getAllQuestions(QuestionSet.valueOf(type));
    }
}
