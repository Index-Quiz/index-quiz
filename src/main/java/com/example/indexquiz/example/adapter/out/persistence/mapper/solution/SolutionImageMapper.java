package com.example.indexquiz.example.adapter.out.persistence.mapper.solution;

import com.example.indexquiz.example.adapter.out.persistence.entity.solution.SolutionImageEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.solution.SolutionImage;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface SolutionImageMapper {

    SolutionImageEntity mapToEntity(SolutionImage solutionImage);

    SolutionImage mapToDomain(SolutionImageEntity solutionImageEntity);
}
