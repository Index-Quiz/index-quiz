package com.example.indexquiz.useranswer.domain;

import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAnswer {

    private final Long id;

    private final long questionId;

    private final long optionId;

    private final String submitId;

    public static UserAnswer from(Question question, QuestionOption option) {
        return new UserAnswer(null, question.getId(), option.getId(), UUID.randomUUID().toString());
    } // TODO: 정팩메 사용하지 않고 mapstruct 컴파일 에러 피하는 방법 찾기
}
