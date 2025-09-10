package com.example.indexquiz.question.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.application.port.in.GetUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class UserAnswerControllerTest extends BaseControllerTest {

    @MockitoBean
    private SaveUserAnswerUseCase saveUserAnswerUseCase;

    @MockitoBean
    private GetUserAnswerUseCase getUserAnswerUseCase;

    @Nested
    class SaveUserAnswer {

        @Test
        void 사용자의_답변을_저장한다() {
            // given
            SaveUserAnswerResponse response = new SaveUserAnswerResponse("submitId");
            given(saveUserAnswerUseCase.saveUserAnswers(any(SaveUserAnswerRequest.class))).willReturn(response);

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

    @Nested
    class GetUserAnswer {

        @Test
        void 사용자_답변의_채점결과를_가져온다() {
            // given
            String submitId = UUID.randomUUID().toString();
            GetUserAnswerResponse response = new GetUserAnswerResponse(true, List.of(1L), List.of(1L), "해설");
            given(getUserAnswerUseCase.getUserAnswer(any(GetUserAnswerRequest.class))).willReturn(response);

            // when
            GetUserAnswerWebResponse getUserAnswerWebResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("submitId", submitId)
                    .when().get("/api/user-answers/{submitId}")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(GetUserAnswerWebResponse.class);

            // then
            assertAll(
                    () -> assertThat(getUserAnswerWebResponse.isCorrect()).isEqualTo(response.isCorrect()),
                    () -> assertThat(getUserAnswerWebResponse.userOptions()).containsExactlyElementsOf(response.userOptions()),
                    () -> assertThat(getUserAnswerWebResponse.answerOptions()).containsExactlyElementsOf(response.answerOptions()),
                    () -> assertThat(getUserAnswerWebResponse.solution()).isEqualTo(response.solution())
            );
        }
    }
}
