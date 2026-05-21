package com.example.indexquiz.learn.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaries;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaryResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnPageResponse;
import com.example.indexquiz.learn.application.port.in.mapper.LearnMaterialDtoMapper;
import com.example.indexquiz.learn.application.port.out.GetLearnMaterialPort;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class LearnMaterialServiceTest extends BaseServiceTest {

    @InjectMocks
    private LearnMaterialService learnMaterialService;

    @Mock
    private GetLearnMaterialPort getLearnMaterialPort;

    @Mock
    private LearnMaterialDtoMapper learnMaterialDtoMapper;

    @Nested
    class GetLearnMaterial {

        @Test
        void 단건_학습자료를_조회한다() {
            // given
            LearnMaterial learnMaterial = DomainFixture.getLearnMaterial(1L, QuestionSet.A, 1);
            GetLearnMaterialResponse response = new GetLearnMaterialResponse(
                    1L, "A", "학습자료 제목1", "학습자료 설명1", "# 학습자료 내용1", 1
            );
            given(getLearnMaterialPort.getById(anyLong())).willReturn(learnMaterial);
            given(learnMaterialDtoMapper.mapToGetLearnMaterialResponse(learnMaterial)).willReturn(response);

            // when
            GetLearnMaterialResponse actual = learnMaterialService.getLearnMaterial(1L);

            // then
            assertThat(actual).isEqualTo(response);
        }
    }

    @Nested
    class GetLearnPageData {

        @Test
        void id가_주어지면_해당_학습자료를_조회한다() {
            // given
            LearnMaterial material1 = DomainFixture.getLearnMaterial(1L, QuestionSet.A, 1);
            LearnMaterial material2 = DomainFixture.getLearnMaterial(2L, QuestionSet.A, 2);
            GetLearnMaterialSummaryResponse summary1 = new GetLearnMaterialSummaryResponse(1L, "학습자료 제목1", "학습자료 설명1", 1);
            GetLearnMaterialSummaryResponse summary2 = new GetLearnMaterialSummaryResponse(2L, "학습자료 제목2", "학습자료 설명2", 2);
            GetLearnMaterialResponse materialResponse = new GetLearnMaterialResponse(
                    2L, "A", "학습자료 제목2", "학습자료 설명2", "# 학습자료 내용2", 2);

            given(getLearnMaterialPort.getAllByQuestionSet(QuestionSet.A)).willReturn(List.of(material1, material2));
            given(learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(material1)).willReturn(summary1);
            given(learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(material2)).willReturn(summary2);
            given(getLearnMaterialPort.getById(2L)).willReturn(material2);
            given(learnMaterialDtoMapper.mapToGetLearnMaterialResponse(material2)).willReturn(materialResponse);

            // when
            GetLearnPageResponse actual = learnMaterialService.getLearnPageData(QuestionSet.A, Optional.of(2L));

            // then
            assertAll(
                    () -> assertThat(actual.summaries()).hasSize(2),
                    () -> assertThat(actual.material()).isPresent(),
                    () -> assertThat(actual.material().get()).isEqualTo(materialResponse)
            );
        }

        @Test
        void id가_없으면_첫번째_학습자료를_조회한다() {
            // given
            LearnMaterial material1 = DomainFixture.getLearnMaterial(1L, QuestionSet.A, 1);
            GetLearnMaterialSummaryResponse summary1 = new GetLearnMaterialSummaryResponse(1L, "학습자료 제목1", "학습자료 설명1", 1);
            GetLearnMaterialResponse materialResponse = new GetLearnMaterialResponse(
                    1L, "A", "학습자료 제목1", "학습자료 설명1", "# 학습자료 내용1", 1);

            given(getLearnMaterialPort.getAllByQuestionSet(QuestionSet.A)).willReturn(List.of(material1));
            given(learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(material1)).willReturn(summary1);
            given(getLearnMaterialPort.getById(1L)).willReturn(material1);
            given(learnMaterialDtoMapper.mapToGetLearnMaterialResponse(material1)).willReturn(materialResponse);

            // when
            GetLearnPageResponse actual = learnMaterialService.getLearnPageData(QuestionSet.A, Optional.empty());

            // then
            assertAll(
                    () -> assertThat(actual.summaries()).hasSize(1),
                    () -> assertThat(actual.material()).isPresent(),
                    () -> assertThat(actual.material().get()).isEqualTo(materialResponse)
            );
        }

        @Test
        void 세트에_학습자료가_없고_id도_없으면_빈_결과를_반환한다() {
            // given
            given(getLearnMaterialPort.getAllByQuestionSet(QuestionSet.A)).willReturn(List.of());

            // when
            GetLearnPageResponse actual = learnMaterialService.getLearnPageData(QuestionSet.A, Optional.empty());

            // then
            assertAll(
                    () -> assertThat(actual.summaries()).isEmpty(),
                    () -> assertThat(actual.material()).isEmpty()
            );
        }
    }

    @Nested
    class GetLearnMaterialsBySet {

        @Test
        void 세트별_학습자료_목록을_조회한다() {
            // given
            LearnMaterial material1 = DomainFixture.getLearnMaterial(1L, QuestionSet.A, 1);
            LearnMaterial material2 = DomainFixture.getLearnMaterial(2L, QuestionSet.A, 2);
            GetLearnMaterialSummaryResponse summary1 = new GetLearnMaterialSummaryResponse(1L, "학습자료 제목1", "학습자료 설명1", 1);
            GetLearnMaterialSummaryResponse summary2 = new GetLearnMaterialSummaryResponse(2L, "학습자료 제목2", "학습자료 설명2", 2);

            given(getLearnMaterialPort.getAllByQuestionSet(QuestionSet.A)).willReturn(List.of(material1, material2));
            given(learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(material1)).willReturn(summary1);
            given(learnMaterialDtoMapper.mapToGetLearnMaterialSummaryResponse(material2)).willReturn(summary2);

            // when
            GetLearnMaterialSummaries actual = learnMaterialService.getLearnMaterialsBySet(QuestionSet.A);

            // then
            assertAll(
                    () -> assertThat(actual.materials()).hasSize(2),
                    () -> assertThat(actual.materials().get(0)).isEqualTo(summary1),
                    () -> assertThat(actual.materials().get(1)).isEqualTo(summary2)
            );
        }
    }
}
