package com.example.indexquiz.solution.adapter.out.mapper;

import com.example.indexquiz.solution.adapter.out.persistence.SolutionEntity;
import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.solution.domain.Solution;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface SolutionMapper {

    SolutionEntity mapToSolutionEntity(Solution solution);

    Solution mapToSolution(SolutionEntity solutionEntity);
}
