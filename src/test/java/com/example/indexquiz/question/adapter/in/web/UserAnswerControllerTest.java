package com.example.indexquiz.question.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.adapter.in.web.dto.request.SaveUserAnswerWebRequest;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetQuestionSetAveragesWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.QuestionSetAverageWebEntry;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.GetVisitorProgressWebResponse;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserResultWebResponse;
import com.example.indexquiz.useranswer.application.port.in.GetQuestionSetAveragesUseCase;
import com.example.indexquiz.useranswer.application.port.in.GetUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.GetVisitorProgressUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserAnswerUseCase;
import com.example.indexquiz.useranswer.application.port.in.SaveUserResultUseCase;
import com.example.indexquiz.useranswer.application.port.in.dto.request.GetUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserAnswerRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.request.SaveUserResultRequest;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.QuestionSetAverage;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SetBestScoreWebEntry;
import com.example.indexquiz.useranswer.domain.SetBestScore;
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
    private SaveUserResultUseCase saveUserResultUseCase;

    @MockitoBean
    private GetUserAnswerUseCase getUserAnswerUseCase;

    @MockitoBean
    private GetQuestionSetAveragesUseCase getQuestionSetAveragesUseCase;

    @MockitoBean
    private GetVisitorProgressUseCase getVisitorProgressUseCase;


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
                    () -> assertThat(getUserAnswerWebResponse.userOptions()).containsExactlyElementsOf(
                            response.userOptions()),
                    () -> assertThat(getUserAnswerWebResponse.answerOptions()).containsExactlyElementsOf(
                            response.answerOptions()),
                    () -> assertThat(getUserAnswerWebResponse.solution()).isEqualTo(response.solution())
            );
        }
    }

    @Nested
    class GetQuestionSetAverages {

        @Test
        void 세트별_평균_점수를_조회한다() {
            // given
            GetQuestionSetAveragesResponse response = new GetQuestionSetAveragesResponse(
                    List.of(
                            new QuestionSetAverage(QuestionSet.A, 4.2),
                            new QuestionSetAverage(QuestionSet.B, 2.9)
                    )
            );
            given(getQuestionSetAveragesUseCase.getQuestionSetAverages()).willReturn(response);

            // when
            GetQuestionSetAveragesWebResponse webResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/api/user-answers/results/averages")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(GetQuestionSetAveragesWebResponse.class);

            // then
            assertAll(
                    () -> assertThat(webResponse.averages()).contains(
                            new QuestionSetAverageWebEntry("A", 4.2),
                            new QuestionSetAverageWebEntry("B", 2.9)
                    )
            );
        }
    }

    @Nested
    class SaveUserResult {

        @Test
        void 사용자의_성적을_저장한다() {
            // given
            SaveUserResultResponse response = new SaveUserResultResponse(
                    1L, QuestionSet.A, 5,
                    List.of(1L, 2L, 3L, 5L, 8L, 10L, 4L, 2L),
                    3.52, 42.0
            );
            given(saveUserResultUseCase.saveUserResult(any(SaveUserResultRequest.class))).willReturn(response);

            // when
            SaveUserResultRequest request = new SaveUserResultRequest(response.questionSetName(), response.score(), null);

            SaveUserResultWebResponse saveUserResultWebResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/user-answers/results")
                    .then().log().all()
                    .statusCode(201)
                    .extract().as(SaveUserResultWebResponse.class);

            // then
            assertAll(
                    () -> assertThat(saveUserResultWebResponse.id()).isEqualTo(response.id()),
                    () -> assertThat(saveUserResultWebResponse.score()).isEqualTo(response.score()),
                    () -> assertThat(saveUserResultWebResponse.questionSetName()).isEqualTo(response.questionSetName().name())
            );
        }
    }

    @Nested
    class GetVisitorProgress {

        @Test
        void 쿠키가_없으면_빈_진행도를_반환한다() {
            GetVisitorProgressWebResponse webResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/api/user-answers/progress")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(GetVisitorProgressWebResponse.class);

            assertAll(
                    () -> assertThat(webResponse.completedSets()).isEmpty(),
                    () -> assertThat(webResponse.progressPercentage()).isEqualTo(0)
            );
        }

        @Test
        void 쿠키가_있으면_방문자의_진행도를_반환한다() {
            // given
            String visitorId = UUID.randomUUID().toString();
            GetVisitorProgressResponse response = new GetVisitorProgressResponse(
                    List.of(
                            new SetBestScore(QuestionSet.A, 6),
                            new SetBestScore(QuestionSet.C, 7)
                    ),
                    25
            );
            given(getVisitorProgressUseCase.getProgress(visitorId)).willReturn(response);

            // when
            GetVisitorProgressWebResponse webResponse = RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("visitor_id", visitorId)
                    .when().get("/api/user-answers/progress")
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(GetVisitorProgressWebResponse.class);

            // then
            assertAll(
                    () -> assertThat(webResponse.completedSets()).containsExactly(
                            new SetBestScoreWebEntry("A", 6),
                            new SetBestScoreWebEntry("C", 7)
                    ),
                    () -> assertThat(webResponse.progressPercentage()).isEqualTo(25)
            );
        }
    }
}
