package com.example.indexquiz.learn.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.indexquiz.BaseRepositoryTest;
import com.example.indexquiz.common.EntityFixture;
import com.example.indexquiz.learn.adapter.out.mapper.LearnMaterialMapper;
import com.example.indexquiz.learn.adapter.out.mapper.LearnMaterialMapperImpl;
import com.example.indexquiz.learn.domain.LearnMaterial;
import com.example.indexquiz.question.domain.QuestionSet;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({
        GetLearnMaterialPersistenceAdapter.class,
        LearnMaterialMapperImpl.class
})
class GetLearnMaterialPersistenceAdapterTest extends BaseRepositoryTest {

    @Autowired
    private GetLearnMaterialPersistenceAdapter getLearnMaterialPersistenceAdapter;

    @Autowired
    private LearnMaterialJpaRepository learnMaterialJpaRepository;

    @Autowired
    private LearnMaterialMapper learnMaterialMapper;

    @Nested
    class GetById {

        @Test
        void 아이디로_학습자료를_가져올_수_있다() {
            LearnMaterialEntity entity = EntityFixture.getLearnMaterialEntity(QuestionSet.A, 1);
            LearnMaterialEntity savedEntity = learnMaterialJpaRepository.save(entity);

            LearnMaterial foundMaterial = getLearnMaterialPersistenceAdapter.getById(savedEntity.getId());

            assertAll(
                    () -> assertThat(foundMaterial.getQuestionSet()).isEqualTo(QuestionSet.A),
                    () -> assertThat(foundMaterial.getTitle()).isEqualTo(entity.getTitle()),
                    () -> assertThat(foundMaterial.getDescription()).isEqualTo(entity.getDescription()),
                    () -> assertThat(foundMaterial.getContent()).isEqualTo(entity.getContent()),
                    () -> assertThat(foundMaterial.getDisplayOrder()).isEqualTo(entity.getDisplayOrder())
            );
        }
    }

    @Nested
    class GetAllByQuestionSet {

        @Test
        void 문제세트별_학습자료를_정렬된_순서로_가져올_수_있다() {
            learnMaterialJpaRepository.save(EntityFixture.getLearnMaterialEntity(QuestionSet.A, 2));
            learnMaterialJpaRepository.save(EntityFixture.getLearnMaterialEntity(QuestionSet.A, 1));
            learnMaterialJpaRepository.save(EntityFixture.getLearnMaterialEntity(QuestionSet.B, 1));

            List<LearnMaterial> materials = getLearnMaterialPersistenceAdapter.getAllByQuestionSet(QuestionSet.A);

            assertAll(
                    () -> assertThat(materials).hasSize(2),
                    () -> assertThat(materials.get(0).getDisplayOrder()).isEqualTo(1),
                    () -> assertThat(materials.get(1).getDisplayOrder()).isEqualTo(2)
            );
        }
    }
}
