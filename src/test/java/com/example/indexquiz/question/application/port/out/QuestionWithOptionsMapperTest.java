package com.example.indexquiz.question.application.port.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionOptionResponse;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponse;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionType;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class QuestionWithOptionsMapperTest extends BaseMapperTest {

    private QuestionWithOptionsMapper questionWithOptionsMapper;

    @BeforeEach
    void setUp() {
        questionWithOptionsMapper = Mappers.getMapper(QuestionWithOptionsMapper.class);
    }

    @Nested
    class MapToGetQuestionResponse {

        @Test
        void 도메인에서_DTO로_매핑할_수_있다() {
            Question question = new Question(1L, QuestionType.SINGLE_CHOICE, "질문 내용", 1L);
            List<QuestionOption> options = List.of(
                    new QuestionOption(1L, 1L, "선택지 내용1", 1L),
                    new QuestionOption(2L, 1L, "선택지 내용2", 2L),
                    new QuestionOption(3L, 1L, "선택지 내용3", 3L));
            QuestionWithOptions questionWithOptions = new QuestionWithOptions(question, options);

            GetQuestionResponse response = questionWithOptionsMapper.mapToGetQuestionResponse(questionWithOptions);

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(question.getId()),
                    () -> assertThat(response.type()).isEqualTo(question.getType()),
                    () -> assertThat(response.content()).isEqualTo(question.getContent()),
                    () -> assertThat(response.options().stream().map(GetQuestionOptionResponse::id))
                            .containsExactlyElementsOf(options.stream().map(QuestionOption::getId).toList()),
                    () -> assertThat(response.options().stream().map(GetQuestionOptionResponse::content))
                            .containsExactlyElementsOf(options.stream().map(QuestionOption::getContent).toList())
            );
        }
    }
}