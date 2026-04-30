package com.example.indexquiz.learn.adapter.out.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseMapperTest;
import com.example.indexquiz.learn.adapter.out.persistence.LearnMaterialEntity;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LearnMaterialMapperTest extends BaseMapperTest {

    private LearnMaterialMapper learnMaterialMapper;

    @BeforeEach
    void setUp() {
        learnMaterialMapper = Mappers.getMapper(LearnMaterialMapper.class);
    }

    @Nested
    class MapToEntity {

        @Test
        void 도메인에서_엔티티로_매핑할_수_있다() {
            LearnMaterial learnMaterial = new LearnMaterial(1L, QuestionSet.A, "제목", "설명", "# 내용", 1);

            LearnMaterialEntity entity = learnMaterialMapper.mapToLearnMaterialEntity(learnMaterial);

            assertAll(
                    () -> assertThat(entity.getId()).isEqualTo(learnMaterial.getId()),
                    () -> assertThat(entity.getQuestionSet()).isEqualTo(learnMaterial.getQuestionSet()),
                    () -> assertThat(entity.getTitle()).isEqualTo(learnMaterial.getTitle()),
                    () -> assertThat(entity.getDescription()).isEqualTo(learnMaterial.getDescription()),
                    () -> assertThat(entity.getContent()).isEqualTo(learnMaterial.getContent()),
                    () -> assertThat(entity.getDisplayOrder()).isEqualTo(learnMaterial.getDisplayOrder())
            );
        }
    }

    @Nested
    class MapToDomain {

        @Test
        void 엔티티에서_도메인으로_매핑할_수_있다() {
            LearnMaterialEntity entity = new LearnMaterialEntity(1L, QuestionSet.A, "제목", "설명", "# 내용", 1);

            LearnMaterial learnMaterial = learnMaterialMapper.mapToLearnMaterial(entity);

            assertAll(
                    () -> assertThat(learnMaterial.getId()).isEqualTo(entity.getId()),
                    () -> assertThat(learnMaterial.getQuestionSet()).isEqualTo(entity.getQuestionSet()),
                    () -> assertThat(learnMaterial.getTitle()).isEqualTo(entity.getTitle()),
                    () -> assertThat(learnMaterial.getDescription()).isEqualTo(entity.getDescription()),
                    () -> assertThat(learnMaterial.getContent()).isEqualTo(entity.getContent()),
                    () -> assertThat(learnMaterial.getDisplayOrder()).isEqualTo(entity.getDisplayOrder())
            );
        }
    }
}
