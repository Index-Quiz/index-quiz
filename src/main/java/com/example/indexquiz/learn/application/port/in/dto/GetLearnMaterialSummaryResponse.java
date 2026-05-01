package com.example.indexquiz.learn.application.port.in.dto;

public record GetLearnMaterialSummaryResponse(
        long id,
        String title,
        String description,
        int displayOrder
) {

}
