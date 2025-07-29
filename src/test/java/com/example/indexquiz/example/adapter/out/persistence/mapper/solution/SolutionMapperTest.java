package com.example.indexquiz.example.adapter.out.persistence.mapper.solution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.solution.SolutionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.BaseMapperTest;
import com.example.indexquiz.example.domain.solution.Solution;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SolutionMapperTest extends BaseMapperTest {

    @Autowired
    private SolutionMapper solutionMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            Solution solution = new Solution(1L, 1L, "해설 내용");

            SolutionEntity solutionEntity = solutionMapper.mapToEntity(solution);

            assertAll(
                    () -> assertThat(solutionEntity.getId()).isEqualTo(solution.getId()),
                    () -> assertThat(solutionEntity.getQuestionId()).isEqualTo(solution.getQuestionId()),
                    () -> assertThat(solutionEntity.getDescription()).isEqualTo(solution.getDescription())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            SolutionEntity solutionEntity = new SolutionEntity(1L, 1L, "해설 내용");

            Solution solution = solutionMapper.mapToDomain(solutionEntity);

            assertAll(
                    () -> assertThat(solution.getId()).isEqualTo(solutionEntity.getId()),
                    () -> assertThat(solution.getQuestionId()).isEqualTo(solutionEntity.getQuestionId()),
                    () -> assertThat(solution.getDescription()).isEqualTo(solutionEntity.getDescription())
            );
        }
    }
}
