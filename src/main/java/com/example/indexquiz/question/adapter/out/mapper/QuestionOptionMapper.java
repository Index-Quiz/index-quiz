package com.example.indexquiz.question.adapter.out.mapper;

import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.question.adapter.out.persistence.QuestionOptionEntity;
import com.example.indexquiz.question.domain.QuestionOption;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface QuestionOptionMapper {

    QuestionOptionEntity mapToQuestionOptionEntity(QuestionOption questionOption);

    QuestionOption mapToQuestionOption(QuestionOptionEntity questionOptionEntity);

    default List<QuestionOption> mapToQuestionOptions(List<QuestionOptionEntity> questionOptionEntities) {
        return questionOptionEntities.stream()
                .map(this::mapToQuestionOption)
                .toList();
    }
}
