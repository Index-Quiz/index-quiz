package com.example.indexquiz.example.adapter.out.persistence.mapper.solution;

import com.example.indexquiz.example.adapter.out.persistence.entity.solution.SolutionEntity;
import com.example.indexquiz.example.adapter.out.persistence.mapper.config.MapperConfiguration;
import com.example.indexquiz.example.domain.solution.Solution;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface SolutionMapper {

    SolutionEntity mapToEntity(Solution solution);

    Solution mapToDomain(SolutionEntity solutionEntity);
}
