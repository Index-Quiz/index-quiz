package com.example.indexquiz.example.domain.solution;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Solution {

    private final Long id;

    private final long questionId;

    private final String description;
}
