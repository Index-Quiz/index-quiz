package com.example.indexquiz.learn.adapter.in.web;

import com.example.indexquiz.learn.application.port.in.LearnMaterialUseCase;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaries;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.question.domain.QuestionSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learn-materials")
public class LearnMaterialController {

    private final LearnMaterialUseCase learnMaterialUseCase;

    @GetMapping("/{id}")
    public GetLearnMaterialResponse getLearnMaterial(@PathVariable(name = "id") long id) {
        return learnMaterialUseCase.getLearnMaterial(id);
    }

    @GetMapping
    public GetLearnMaterialSummaries getLearnMaterialsBySet(
            @RequestParam(name = "set") QuestionSet questionSet) {
        return learnMaterialUseCase.getLearnMaterialsBySet(questionSet);
    }
}
