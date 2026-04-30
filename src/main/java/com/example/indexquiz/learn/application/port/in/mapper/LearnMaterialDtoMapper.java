package com.example.indexquiz.learn.application.port.in.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialResponse;
import com.example.indexquiz.learn.application.port.in.dto.GetLearnMaterialSummaryResponse;
import com.example.indexquiz.learn.domain.LearnMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface LearnMaterialDtoMapper {

    @Mapping(source = "questionSet", target = "questionSet")
    GetLearnMaterialResponse mapToGetLearnMaterialResponse(LearnMaterial learnMaterial);

    GetLearnMaterialSummaryResponse mapToGetLearnMaterialSummaryResponse(LearnMaterial learnMaterial);
}
