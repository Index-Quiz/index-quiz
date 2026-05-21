package com.example.indexquiz.adapter.in.web;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.learn.application.port.in.LearnMaterialUseCase;
import com.example.indexquiz.question.application.port.in.QuestionUseCase;
import com.example.indexquiz.question.application.port.in.RefreshDifficultQuestionCacheUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionOptionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.question.domain.QuestionType;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class QuizViewControllerTest extends BaseControllerTest {

    @MockitoBean
    private QuestionUseCase questionUseCase;

    @MockitoBean
    private LearnMaterialUseCase learnMaterialUseCase;

    @MockitoBean
    private RefreshDifficultQuestionCacheUseCase refreshDifficultQuestionCacheUseCase;

    @Nested
    class Quiz {

        @Test
        void 퀴즈_페이지를_정상_렌더링한다() {
            // given
            GetQuestionResponses responses = new GetQuestionResponses(List.of(
                    new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "질문 내용1",
                            List.of(new GetQuestionOptionResponse(1L, "선택지 내용1")))
            ));
            given(questionUseCase.getQuestionsBySet(QuestionSet.A)).willReturn(responses);

            // when
            RestAssured.given().log().all()
                    .queryParam("set", "A")
                    .when().get("/quiz")
                    .then().log().all()
                    .statusCode(200);

            // then
            then(questionUseCase).should().getQuestionsBySet(QuestionSet.A);
        }

        @Test
        void 어려운_문제_세트_퀴즈_페이지를_정상_렌더링한다() {
            // given
            GetQuestionResponses responses = new GetQuestionResponses(List.of(
                    new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "어려운 질문",
                            List.of(new GetQuestionOptionResponse(1L, "선택지 내용1")))
            ));
            given(questionUseCase.getQuestionsBySet(QuestionSet.BEST_DIFFICULT)).willReturn(responses);

            // when
            RestAssured.given().log().all()
                    .queryParam("set", "BEST_DIFFICULT")
                    .when().get("/quiz")
                    .then().log().all()
                    .statusCode(200);

            // then
            then(questionUseCase).should().getQuestionsBySet(QuestionSet.BEST_DIFFICULT);
        }
    }
}
