package com.example.indexquiz.question.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.question.application.port.in.QuestionUseCase;
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

class QuestionControllerTest extends BaseControllerTest {

    @MockitoBean
    private QuestionUseCase questionUseCase;

    @Nested
    class GetQuestion {

        @Test
        void 단건의_질문을_조회한다() {
            // given
            GetQuestionResponse expected = new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "질문 내용",
                    List.of(new GetQuestionOptionResponse(11L, "선택지 내용1"),
                            new GetQuestionOptionResponse(12L, "선택지 내용2"),
                            new GetQuestionOptionResponse(13L, "선택지 내용3")));
            given(questionUseCase.getQuestion(anyLong())).willReturn(expected);

            // when
            GetQuestionResponse actual = RestAssured.given().log().all()
                    .pathParam("questionId", 1)
                    .when().get("/api/questions/{questionId}")
                    .then().log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getObject("", GetQuestionResponse.class);

            // then
            assertAll(
                    () -> then(questionUseCase).should().getQuestion(1),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }
    }

    @Nested
    class GetAllQuestion {

        @Test
        void 다건의_질문을_조회한다() {
            // given
            GetQuestionResponses expected = new GetQuestionResponses(List.of(
                    new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "질문 내용1",
                    List.of(new GetQuestionOptionResponse(11L, "선택지 내용11"),
                            new GetQuestionOptionResponse(12L, "선택지 내용12"),
                            new GetQuestionOptionResponse(13L, "선택지 내용13"))),
                    new GetQuestionResponse(2L, QuestionType.SINGLE_CHOICE, "질문 내용2",
                            List.of(new GetQuestionOptionResponse(21L, "선택지 내용21"),
                                    new GetQuestionOptionResponse(22L, "선택지 내용22"),
                                    new GetQuestionOptionResponse(23L, "선택지 내용23")))
            ));

            given(questionUseCase.getAllQuestions(QuestionSet.BEST_DIFFICULT)).willReturn(expected);

            // when
            GetQuestionResponses actual = RestAssured.given().log().all()
                    .queryParam("type", "BEST_DIFFICULT")
                    .when().get("/api/questions")
                    .then().log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getObject("", GetQuestionResponses.class);

            // then
            assertAll(
                    () -> then(questionUseCase).should().getAllQuestions(QuestionSet.BEST_DIFFICULT),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }
    }
}
