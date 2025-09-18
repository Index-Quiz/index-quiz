package com.example.indexquiz.useranswer.adapter.in.web.dto.response;

public record SaveUserResultWebResponse(
        long id,
        String questionSetName,
        int score
) {

}
