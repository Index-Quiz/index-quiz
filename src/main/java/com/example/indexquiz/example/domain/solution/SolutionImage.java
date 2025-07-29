package com.example.indexquiz.example.domain.solution;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SolutionImage {

    private final Long id;
    private final long solutionId;
    private final String url;
}
