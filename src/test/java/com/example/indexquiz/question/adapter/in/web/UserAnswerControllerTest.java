package com.example.indexquiz.question.adapter.in.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.useranswer.application.port.in.UserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class UserAnswerControllerTest extends BaseControllerTest {

    @MockitoBean
    private UserAnswerUseCase userAnswerUseCase;

    @Nested
    class SaveUserAnswer {

        @Test
        void 사용자의_답변을_저장한다() {
            // given
            willDoNothing().given(userAnswerUseCase).saveUserAnswers(anyLong(), any());

            // when
            SaveUserAnswerRequest request = new SaveUserAnswerRequest(List.of(1L, 2L));

            RestAssured.given().log().all()
                    .pathParam("questionId", 1)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/questions/{questionId}/userAnswers")
                    .then().log().all()
                    .statusCode(200);

            // then
            then(userAnswerUseCase).should().saveUserAnswers(1, request);
        }
    }
}
