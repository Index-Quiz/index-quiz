package com.example.indexquiz.useranswer.application.port.in.mapper;

import com.example.indexquiz.answer.domain.Answers;
import com.example.indexquiz.common.mapper.MapperConfiguration;
import com.example.indexquiz.solution.domain.Solution;
import com.example.indexquiz.useranswer.application.port.in.dto.response.GetUserAnswerResponse;
import com.example.indexquiz.useranswer.application.port.in.dto.response.SaveUserAnswerResponse;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import org.mapstruct.Mapper;

//port dto <-> domain
@Mapper(config = MapperConfiguration.class)
public interface UserAnswerDtoMapper {

    SaveUserAnswerResponse mapToSaveUserAnswerResponse(UserAnswers userAnswers);

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
