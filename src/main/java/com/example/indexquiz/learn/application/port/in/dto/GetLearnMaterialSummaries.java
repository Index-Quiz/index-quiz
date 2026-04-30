package com.example.indexquiz.learn.application.port.in.dto;

import java.util.List;

public record GetLearnMaterialSummaries(
        List<GetLearnMaterialSummaryResponse> materials
) {

}
