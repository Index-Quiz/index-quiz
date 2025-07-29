package com.example.indexquiz.example.adapter.out.persistence.mapper.solution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.example.adapter.out.persistence.entity.solution.SolutionImageEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.BaseMapperTest;
import com.example.indexquiz.example.domain.solution.SolutionImage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SolutionImageMapperTest extends BaseMapperTest {

    @Autowired
    private SolutionImageMapper solutionImageMapper;

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            SolutionImage solutionImage = new SolutionImage(1L, 1L, "http://example.com");

            SolutionImageEntity imageEntity = solutionImageMapper.mapToEntity(solutionImage);

            assertAll(
                    () -> assertThat(imageEntity.getId()).isEqualTo(solutionImage.getId()),
                    () -> assertThat(imageEntity.getSolutionId()).isEqualTo(solutionImage.getSolutionId()),
                    () -> assertThat(imageEntity.getUrl()).isEqualTo(solutionImage.getUrl())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            SolutionImageEntity solutionImageEntity = new SolutionImageEntity(1L, 1L, "http://example.com");

            SolutionImage solutionImage = solutionImageMapper.mapToDomain(solutionImageEntity);

            assertAll(
                    () -> assertThat(solutionImage.getId()).isEqualTo(solutionImageEntity.getId()),
                    () -> assertThat(solutionImage.getSolutionId()).isEqualTo(solutionImageEntity.getSolutionId()),
                    () -> assertThat(solutionImage.getUrl()).isEqualTo(solutionImageEntity.getUrl())
            );
        }
    }
}
