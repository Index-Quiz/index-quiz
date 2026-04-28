package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetQuestionSetAveragesResponse;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class QuestionSetAveragesServiceTest extends BaseServiceTest {

    @Autowired
    private QuestionSetAveragesService questionSetAveragesService;

    @MockitoSpyBean
    private GetQuestionSetAveragesPort getQuestionSetAveragesPort;

    @Nested
    class GetQuestionSetAverages {

        @Test
        void 데이터가_없을_때_0점을_반환한다() {
            // given
            for (QuestionSet questionSet : QuestionSet.values()) {
                given(getQuestionSetAveragesPort.getAverageScore(questionSet, questionSet.getQuestionCount()))
                        .willReturn(Optional.empty());
            }

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertAll(
                    () -> assertThat(response.averages()).hasSize(QuestionSet.values().length),
                    () -> assertThat(response.averages().get(QuestionSet.A)).isEqualTo(0.0)
            );
        }

        @Test
        void 단일_세트의_평균을_소수점_1자리로_반올림하여_반환한다() {
            // given
            for (QuestionSet questionSet : QuestionSet.values()) {
                given(getQuestionSetAveragesPort.getAverageScore(questionSet, questionSet.getQuestionCount()))
                        .willReturn(Optional.empty());
            }
            given(getQuestionSetAveragesPort.getAverageScore(QuestionSet.A, QuestionSet.A.getQuestionCount()))
                    .willReturn(Optional.of(4.56));

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertAll(
                    () -> assertThat(response.averages()).hasSize(QuestionSet.values().length),
                    () -> assertThat(response.averages().get(QuestionSet.A)).isEqualTo(4.6)
            );
        }

        @Test
        void 복수_세트의_평균을_반환한다() {
            // given
            for (QuestionSet questionSet : QuestionSet.values()) {
                given(getQuestionSetAveragesPort.getAverageScore(questionSet, questionSet.getQuestionCount()))
                        .willReturn(Optional.empty());
            }
            given(getQuestionSetAveragesPort.getAverageScore(QuestionSet.A, QuestionSet.A.getQuestionCount()))
                    .willReturn(Optional.of(4.56));
            given(getQuestionSetAveragesPort.getAverageScore(QuestionSet.B, QuestionSet.B.getQuestionCount()))
                    .willReturn(Optional.of(2.93));
            given(getQuestionSetAveragesPort.getAverageScore(QuestionSet.C, QuestionSet.C.getQuestionCount()))
                    .willReturn(Optional.of(6.17));

            // when
            GetQuestionSetAveragesResponse response = questionSetAveragesService.getQuestionSetAverages();

            // then
            assertAll(
                    () -> assertThat(response.averages()).hasSize(QuestionSet.values().length),
                    () -> assertThat(response.averages().get(QuestionSet.A)).isEqualTo(4.6),
                    () -> assertThat(response.averages().get(QuestionSet.B)).isEqualTo(2.9),
                    () -> assertThat(response.averages().get(QuestionSet.C)).isEqualTo(6.2)
            );
        }
    }
}
