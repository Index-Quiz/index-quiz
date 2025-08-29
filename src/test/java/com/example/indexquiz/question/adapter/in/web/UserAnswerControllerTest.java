package com.example.indexquiz.question.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.application.port.in.UserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
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
            SaveUserAnswerResponse response = new SaveUserAnswerResponse("submitId");
            given(userAnswerUseCase.saveUserAnswers(any(SaveUserAnswerRequest.class))).willReturn(response);

            // when
            SaveUserAnswerWebRequest request = new SaveUserAnswerWebRequest(1L, List.of(1L, 2L));

            SaveUserAnswerWebResponse saveUserAnswerWebResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/user-answers")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(SaveUserAnswerWebResponse.class);

            // then
            assertThat(saveUserAnswerWebResponse.submitId()).isEqualTo(response.submitId());
        }
    }
}
