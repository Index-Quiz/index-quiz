package com.example.indexquiz.learn.application.port.in.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaryResponse;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LearnMaterialDtoMapperTest extends BaseMapperTest {

    private LearnMaterialDtoMapper learnMaterialDtoMapper;

    @BeforeEach
    void setUp() {
        learnMaterialDtoMapper = Mappers.getMapper(LearnMaterialDtoMapper.class);
    }

    @Nested
    class MapToGetLearnMaterialResponse {

        @Test
        void 도메인에서_상세_응답으로_매핑할_수_있다() {
            LearnMaterial learnMaterial = new LearnMaterial(1L, QuestionSet.A, "제목", "설명", "# 내용", 1);

            GetLearnMaterialResponse response = learnMaterialDtoMapper.mapToGetLearnMaterialResponse(learnMaterial);

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(learnMaterial.getId()),
                    () -> assertThat(response.questionSet()).isEqualTo(learnMaterial.getQuestionSet().name()),
                    () -> assertThat(response.title()).isEqualTo(learnMaterial.getTitle()),
                    () -> assertThat(response.description()).isEqualTo(learnMaterial.getDescription()),
                    () -> assertThat(response.content()).isEqualTo(learnMaterial.getContent()),
                    () -> assertThat(response.displayOrder()).isEqualTo(learnMaterial.getDisplayOrder())
            );
        }
    }

    @Nested
    class MapToGetLearnMaterialSummaryResponse {

        @Test
        void 도메인에서_요약_응답으로_매핑할_수_있다() {
            LearnMaterial learnMaterial = new LearnMaterial(1L, QuestionSet.A, "제목", "설명", "# 내용", 1);

            GetLearnMaterialSummaryResponse response = learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(learnMaterial);

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(learnMaterial.getId()),
                    () -> assertThat(response.title()).isEqualTo(learnMaterial.getTitle()),
                    () -> assertThat(response.description()).isEqualTo(learnMaterial.getDescription()),
                    () -> assertThat(response.displayOrder()).isEqualTo(learnMaterial.getDisplayOrder())
            );
        }
    }
}
