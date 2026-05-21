package com.example.indexquiz.learn.application.port.in.dto;

import java.util.List;
import java.util.Optional;

public record GetLearnPageResponse(
        List<GetLearnMaterialSummaryResponse> summaries,
        Optional<GetLearnMaterialResponse> material
) {

}
