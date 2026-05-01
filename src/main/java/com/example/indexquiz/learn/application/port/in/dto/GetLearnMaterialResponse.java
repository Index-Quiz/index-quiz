package com.example.indexquiz.learn.application.port.in.dto;

public record GetLearnMaterialResponse(
        long id,
        String questionSet,
        String title,
        String description,
        String content,
        int displayOrder
) {

}
