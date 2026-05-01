package com.example.indexquiz.useranswer.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetVisitorProgressResponse;
import com.example.indexquiz.useranswer.application.port.out.GetVisitorProgressPort;
import com.example.indexquiz.useranswer.domain.VisitorProgress;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class VisitorProgressServiceTest extends BaseServiceTest {

    @Autowired
    private VisitorProgressService visitorProgressService;

    @MockitoSpyBean
    private GetVisitorProgressPort getVisitorProgressPort;

    @Nested
    class GetProgress {

        @Test
        void 방문자의_학습_진행도를_반환한다() {
            // given
            String visitorId = "test-visitor-id";
            VisitorProgress progress = new VisitorProgress(Map.of(
                    QuestionSet.A, 6,
                    QuestionSet.C, 7
            ));
            given(getVisitorProgressPort.getByVisitorId(visitorId)).willReturn(progress);

            // when
            GetVisitorProgressResponse response = visitorProgressService.getProgress(visitorId);

            // then
            assertAll(
                    () -> assertThat(response.completedSetBestScore()).hasSize(2),
                    () -> assertThat(response.completedSetBestScore()).containsEntry(QuestionSet.A, 6),
                    () -> assertThat(response.completedSetBestScore()).containsEntry(QuestionSet.C, 7),
                    () -> assertThat(response.progressPercentage()).isEqualTo(25)
            );
        }

        @Test
        void 완료된_세트가_없으면_빈_결과를_반환한다() {
            // given
            String visitorId = "new-visitor-id";
            VisitorProgress progress = new VisitorProgress(Map.of());
            given(getVisitorProgressPort.getByVisitorId(visitorId)).willReturn(progress);

            // when
            GetVisitorProgressResponse response = visitorProgressService.getProgress(visitorId);

            // then
            assertAll(
                    () -> assertThat(response.completedSetBestScore()).isEmpty(),
                    () -> assertThat(response.progressPercentage()).isEqualTo(0)
            );
        }
    }
}
