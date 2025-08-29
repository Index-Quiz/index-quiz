package com.example.indexquiz.question.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionOptionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.application.port.in.mapper.QuestionWithOptionsMapper;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionType;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class QuestionServiceTest extends BaseServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private GetQuestionPort getQuestionPort;

    @Mock
    private QuestionWithOptionsMapper questionWithOptionsMapper;

    @Nested
    class GetQuestion {

        @Test
        void 단건_질문을_조회한다() {
            // given
            Question question = DomainFixture.getQuestion(1);
            List<QuestionOption> options = DomainFixture.getQuestionOptions(question.getId(), 3);
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);
            GetQuestionResponse response = new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "질문 내용",
                    List.of(new GetQuestionOptionResponse(1L, "선택지 내용1"),
                            new GetQuestionOptionResponse(2L, "선택지 내용2"),
                            new GetQuestionOptionResponse(3L, "선택지 내용3")));
            given(getQuestionPort.getQuestionWithOptions(anyLong())).willReturn(questionWithOptions);
            given(questionWithOptionsMapper.mapToGetQuestionResponse(questionWithOptions)).willReturn(response);

            // when
            GetQuestionResponse actual = questionService.getQuestion(1L);

            // then
            assertThat(actual).isEqualTo(response);
        }
    }
}
