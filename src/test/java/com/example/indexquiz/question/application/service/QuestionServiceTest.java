package com.example.indexquiz.question.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.indexquiz.BaseServiceTest;
import com.example.indexquiz.common.DomainFixture;
import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionOptionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.application.port.in.mapper.QuestionWithOptionsMapper;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.question.domain.QuestionType;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import com.example.indexquiz.useranswer.application.port.out.GetDifficultQuestionPort;
import com.example.indexquiz.useranswer.application.port.out.dto.DifficultQuestionResponses;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class QuestionServiceTest extends BaseServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private GetQuestionPort getQuestionPort;

    @Mock
    private GetDifficultQuestionPort getDifficultQuestionPort;

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
            given(getQuestionPort.getQuestionWithOptionsByOrder(anyLong())).willReturn(questionWithOptions);
            given(questionWithOptionsMapper.mapToGetQuestionResponse(questionWithOptions)).willReturn(response);

            // when
            GetQuestionResponse actual = questionService.getQuestion(1L);

            // then
            assertThat(actual).isEqualTo(response);
        }
    }

    @Nested
    class GetAllQuestion {

        @Test
        void 어려운_문제_세트의_모든_질문을_조회만_허용한다() {
            // given
            Question question1 = DomainFixture.getQuestion(1);
            Question question2 = DomainFixture.getQuestion(2);
            List<QuestionOption> options1 = DomainFixture.getQuestionOptions(question1.getId(), 3);
            List<QuestionOption> options2 = DomainFixture.getQuestionOptions(question2.getId(), 3);
            List<QuestionWithOptions> questionWithOptions = List.of(
                    new QuestionWithOptions(question1, options1),
                    new QuestionWithOptions(question2, options2)
            );
            GetQuestionResponses response = new GetQuestionResponses(List.of(
                    new GetQuestionResponse(1L, QuestionType.SINGLE_CHOICE, "질문 내용1",
                    List.of(new GetQuestionOptionResponse(1L, "선택지 내용1"),
                            new GetQuestionOptionResponse(2L, "선택지 내용2"),
                            new GetQuestionOptionResponse(3L, "선택지 내용3"))),
                    new GetQuestionResponse(2L, QuestionType.SINGLE_CHOICE, "질문 내용2",
                            List.of(new GetQuestionOptionResponse(4L, "선택지 내용1"),
                                    new GetQuestionOptionResponse(5L, "선택지 내용2"),
                                    new GetQuestionOptionResponse(6L, "선택지 내용3")))
            ));
            DifficultQuestionResponses mockDifficultQuestionResponses = new DifficultQuestionResponses(List.of(
                    question1.getId(), question2.getId()
            ));
            given(getDifficultQuestionPort.findDifficultQuestions(anyLong())).willReturn(mockDifficultQuestionResponses);
            given(getQuestionPort.getAllQuestionWithOptions(anyList())).willReturn(questionWithOptions);
            given(questionWithOptionsMapper.mapToGetQuestionResponses(questionWithOptions)).willReturn(response);

            // when
            GetQuestionResponses actual = questionService.getAllQuestions(QuestionSet.BEST_DIFFICULT);

            // then
            assertThat(actual).isEqualTo(response);
        }

        @ParameterizedTest
        @EnumSource(value = QuestionSet.class, mode = Mode.EXCLUDE, names = "BEST_DIFFICULT")
        void 어려운_문제_세트외의_다른_세트의_전문제_조회를_허용하지_않는다(QuestionSet questionSet) {
            assertThatThrownBy(() -> questionService.getAllQuestions(questionSet))
                    .isInstanceOf(IndexQuizException.class)
                    .hasMessage(ErrorCode.QUESTION_SET_BAD_REQUEST.getMessage());
        }
    }
}
