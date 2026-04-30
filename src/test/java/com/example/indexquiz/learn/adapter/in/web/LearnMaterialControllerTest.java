package com.example.indexquiz.learn.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.indexquiz.BaseControllerTest;
import com.example.indexquiz.learn.application.port.in.LearnMaterialUseCase;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialListResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaryResponse;
import com.example.indexquiz.question.domain.QuestionSet;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class LearnMaterialControllerTest extends BaseControllerTest {

    @MockitoBean
    private LearnMaterialUseCase learnMaterialUseCase;

    @Nested
    class GetLearnMaterial {

        @Test
        void 단건_학습자료를_조회한다() {
            // given
            GetLearnMaterialResponse expected = new GetLearnMaterialResponse(
                    1L, "A", "인덱스는 왜 필요한가?", "풀 테이블 스캔의 문제점과 인덱스의 역할", "# 학습 내용", 1
            );
            given(learnMaterialUseCase.getLearnMaterial(anyLong())).willReturn(expected);

            // when
            GetLearnMaterialResponse actual = RestAssured.given().log().all()
                    .pathParam("id", 1)
                    .when().get("/api/learn-materials/{id}")
                    .then().log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getObject("", GetLearnMaterialResponse.class);

            // then
            assertAll(
                    () -> then(learnMaterialUseCase).should().getLearnMaterial(1L),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }
    }

    @Nested
    class GetLearnMaterialsBySet {

        @Test
        void 세트별_학습자료_목록을_조회한다() {
            // given
            GetLearnMaterialListResponse expected = new GetLearnMaterialListResponse(List.of(
                    new GetLearnMaterialSummaryResponse(1L, "인덱스는 왜 필요한가?", "풀 테이블 스캔의 문제점", 1),
                    new GetLearnMaterialSummaryResponse(2L, "해쉬 인덱스의 구조", "해쉬 함수 기반 인덱스", 2)
            ));
            given(learnMaterialUseCase.getLearnMaterialsBySet(QuestionSet.A)).willReturn(expected);

            // when
            GetLearnMaterialListResponse actual = RestAssured.given().log().all()
                    .queryParam("set", "A")
                    .when().get("/api/learn-materials")
                    .then().log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getObject("", GetLearnMaterialListResponse.class);

            // then
            assertAll(
                    () -> then(learnMaterialUseCase).should().getLearnMaterialsBySet(QuestionSet.A),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }
    }
}
