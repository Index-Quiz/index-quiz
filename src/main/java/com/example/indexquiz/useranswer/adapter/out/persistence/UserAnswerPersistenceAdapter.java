package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapper;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserResultMapper;
import com.example.indexquiz.question.application.port.out.GetDifficultQuestionPort;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.out.GetScoreDistributionPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.application.port.out.GetUserAnswerPort;
import com.example.indexquiz.question.application.port.out.dto.DifficultQuestionResponses;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.ScoreDistribution;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import com.example.indexquiz.useranswer.domain.UserResult;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAnswerPersistenceAdapter implements
        SaveUserAnswerPort,
        SaveUserResultPort,
        GetUserAnswerPort,
        GetDifficultQuestionPort,
        GetScoreDistributionPort
{

    private final UserAnswerJpaRepository userAnswerJpaRepository;

    private final UserResultJpaRepository userResultJpaRepository;

    private final UserAnswerMapper userAnswerMapper;

    private final UserResultMapper userResultMapper;

    @Override
    public UserAnswers saveUserAnswers(SaveUserAnswersCommand command) {
        List<UserAnswerEntity> userAnswers = command.getUserAnswers().stream()
                .map(userAnswerMapper::mapToUserAnswerEntity)
                .toList();
        return userAnswerJpaRepository.saveAll(userAnswers)
                .stream()
                .map(userAnswerMapper::mapToUserAnswer)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), UserAnswers::new));
    }

    @Override
    public UserAnswers getBySubmitId(String submitId) {
        return userAnswerJpaRepository.findAllBySubmitId(submitId)
                .stream()
                .map(userAnswerMapper::mapToUserAnswer)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), UserAnswers::new));
    }

    @Override
    public DifficultQuestionResponses findDifficultQuestions(long problemCount) {
        List<Long> difficutQuestionIds = userAnswerJpaRepository.findDifficultQuestions(problemCount);
        return new DifficultQuestionResponses(difficutQuestionIds);
    }

    @Override
    public UserResult saveUserResult(UserResult userResult) {
        UserResultEntity userResultEntity = userResultMapper.mapToUserResultEntity(userResult);
        UserResultEntity savedUserResultEntity = userResultJpaRepository.save(userResultEntity);
        return userResultMapper.mapToUserResult(savedUserResultEntity);
    }

    @Override
    public ScoreDistribution getScoreDistribution(QuestionSet questionSet, int userScore) {
        List<Object[]> rows = userResultJpaRepository.countByQuestionSetGroupByScore(questionSet);

        long[] distribution = new long[8];
        long totalCount = 0;
        long scoredGreaterOrEqual = 0;
        long totalScoreSum = 0;

        for (Object[] row : rows) {
            int score = (int) row[0];
            long count = (long) row[1];
            if (score >= 0 && score <= 7) {
                distribution[score] = count;
            }
            totalCount += count;
            totalScoreSum += (long) score * count;
            if (score >= userScore) {
                scoredGreaterOrEqual += count;
            }
        }

        double average = totalCount > 0 ? (double) totalScoreSum / totalCount : 0.0;
        double topPercentage = totalCount > 0 ? ((double) scoredGreaterOrEqual / totalCount) * 100.0 : 100.0;

        return new ScoreDistribution(
                Arrays.stream(distribution).boxed().toList(),
                Math.round(average * 100.0) / 100.0,
                Math.round(topPercentage * 10.0) / 10.0
        );
    }
}
