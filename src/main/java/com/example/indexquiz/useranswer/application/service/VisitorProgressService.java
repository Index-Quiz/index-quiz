package com.example.indexquiz.useranswer.application.service;

import com.example.indexquiz.useranswer.application.port.in.GetVisitorProgressUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;
import com.example.indexquiz.useranswer.application.port.out.GetVisitorProgressPort;
import com.example.indexquiz.useranswer.domain.VisitorProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorProgressService implements GetVisitorProgressUseCase {

    private final GetVisitorProgressPort getVisitorProgressPort;

    @Override
    public GetVisitorProgressResponse getProgress(String visitorId) {
        VisitorProgress progress = getVisitorProgressPort.getByVisitorId(visitorId);
        return new GetVisitorProgressResponse(
                progress.getCompletedSets(),
                progress.progressPercentage()
        );
    }
}
