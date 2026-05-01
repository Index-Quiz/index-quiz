package com.example.indexquiz.learn.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.learn.adapter.out.persistence.LearnMaterialEntity;
import com.example.indexquiz.learn.domain.LearnMaterial;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface LearnMaterialMapper {

    LearnMaterial mapToLearnMaterial(LearnMaterialEntity learnMaterialEntity);
}
