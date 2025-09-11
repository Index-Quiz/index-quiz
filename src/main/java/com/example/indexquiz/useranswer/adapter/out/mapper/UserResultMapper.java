package com.example.indexquiz.useranswer.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.useranswer.adapter.out.persistence.UserResultEntity;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserResultMapper {

    UserResult mapToUserResult(UserResultEntity userResultEntity);

    UserResultEntity mapToUserResultEntity(UserResult userResult);

}
