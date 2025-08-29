package com.example.indexquiz.useranswer.adapter.in.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.indexquiz.solution.adapter.out.mapper.SolutionMapper;
import com.example.indexquiz.solution.adapter.out.persistence.SolutionEntity;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.adapter.in.web.dto.response.SaveUserAnswerWebResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.SaveUserAnswerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserAnswerWebMapperTest {

    private UserAnswerWebMapper userAnswerWebMapper;

    @BeforeEach
    void setUp() {
        userAnswerWebMapper = Mappers.getMapper(UserAnswerWebMapper.class);
    }

    @Nested
    class MapToSaveUserAnswerWebResponse {

        @Test
        void 어플리메이션_dto에서_web_dto로_매핑할_수_있다() {
            SaveUserAnswerResponse origin = new SaveUserAnswerResponse("example");

            SaveUserAnswerWebResponse target = userAnswerWebMapper.mapToSaveUserAnswerWebResponse(origin);

            assertThat(target.submitId()).isEqualTo(origin.submitId());
        }
    }
}
