package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface UserResultDtoMapper {

    @Mapping(source = "userResult.questionSet", target = "questionSetName")
    @Mapping(source = "userResult.id", target = "id")
    @Mapping(source = "userResult.score", target = "score")
    @Mapping(source = "distribution.distribution", target = "scoreDistribution")
    @Mapping(source = "distribution.averageScore", target = "averageScore")
    @Mapping(source = "distribution.topPercentage", target = "topPercentage")
    SaveUserResultResponse mapToSaveUserResultResponse(UserResult userResult, ScoreDistribution distribution);
}
