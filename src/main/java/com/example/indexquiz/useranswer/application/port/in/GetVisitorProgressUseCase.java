package com.example.indexquiz.useranswer.application.port.in;

import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;

public interface GetVisitorProgressUseCase {

    GetVisitorProgressResponse getProgress(String visitorId);
}
