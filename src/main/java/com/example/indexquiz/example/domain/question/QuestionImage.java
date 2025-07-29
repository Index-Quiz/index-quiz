package com.example.indexquiz.example.domain.question;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionImage {

    private final Long id;
    private final long questionId;
    private final String url;
}
