package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
import com.example.indexquiz.useranswer.domain.QuestionSetAverage;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.mockito.BDDMockito.willReturn;

class QuestionSetAveragesServiceTest extends BaseServiceTest {

    @Autowired
    private QuestionSetAveragesService questionSetAveragesService;

    @MockitoSpyBean
    private GetQuestionSetAveragesPort getQuestionSetAveragesPort;

    @Nested
    class GetQuestionSetAverages {

        @Test
        void 빈_리스트일_때_빈_맵을_반환한다() {
            // given
            willReturn(List.of()).given(getQuestionSetAveragesPort).getAverageScoresByQuestionSet();

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertThat(response.averages()).isEmpty();
        }

        @Test
        void 단일_세트의_평균을_반환한다() {
            // given
            List<QuestionSetAverage> averages = List.of(
                    new QuestionSetAverage(QuestionSet.A, 4.56)
            );
            willReturn(averages).given(getQuestionSetAveragesPort).getAverageScoresByQuestionSet();

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertAll(
                    () -> assertThat(response.averages()).hasSize(1),
                    () -> assertThat(response.averages().get(QuestionSet.A)).isEqualTo(4.6)
            );
        }

        @Test
        void 복수_세트의_평균을_반환한다() {
            // given
            List<QuestionSetAverage> averages = List.of(
                    new QuestionSetAverage(QuestionSet.A, 4.56),
                    new QuestionSetAverage(QuestionSet.B, 2.93),
                    new QuestionSetAverage(QuestionSet.C, 6.17)
            );
            willReturn(averages).given(getQuestionSetAveragesPort).getAverageScoresByQuestionSet();

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertAll(
                    () -> assertThat(response.averages()).hasSize(3),
                    () -> assertThat(response.averages().get(QuestionSet.A)).isEqualTo(4.6),
                    () -> assertThat(response.averages().get(QuestionSet.B)).isEqualTo(2.9),
                    () -> assertThat(response.averages().get(QuestionSet.C)).isEqualTo(6.2)
            );
        }
    }
}
