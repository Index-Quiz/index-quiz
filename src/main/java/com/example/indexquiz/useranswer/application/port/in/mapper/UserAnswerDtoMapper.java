package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserResultResponse;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//port dto <-> domain
@Mapper(config = MapperConfiguration.class)
public interface UserAnswerDtoMapper {

    SaveUserAnswerResponse mapToSaveUserAnswerResponse(UserAnswers userAnswers);

    @Mapping(source = "questionSet", target = "questionSetName")
    SaveUserResultResponse mapToSaveUserResultResponse(UserResult userResult);

    default GetUserAnswerResponse mapToGetUserAnswerResponse(
            UserAnswers userAnswers,
            Answers answers,
            Solution solution
    ) {
       return new GetUserAnswerResponse(
               userAnswers.isCorrect(answers.getAnswerOptions()),
               userAnswers.getUserOptions(),
               answers.getAnswerOptions(),
               solution.getDescription()
       );
    }
}
