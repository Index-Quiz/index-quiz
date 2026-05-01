package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.useranswer.adapter.out.mapper.UserAnswerMapper;
import com.example.indexquiz.useranswer.adapter.out.mapper.UserResultMapper;
import com.example.indexquiz.useranswer.adapter.out.mapper.VisitorQuestionCompletionMapper;
import com.example.indexquiz.question.application.port.out.GetDifficultQuestionPort;
import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.application.port.out.GetQuestionSetAveragesPort;
import com.example.indexquiz.useranswer.application.port.out.GetScoreDistributionPort;
import com.example.indexquiz.useranswer.application.port.out.GetVisitorProgressPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserAnswerPort;
import com.example.indexquiz.useranswer.application.port.out.SaveUserResultPort;
import com.example.indexquiz.useranswer.application.port.out.SaveVisitorQuestionCompletionPort;
import com.example.indexquiz.useranswer.application.port.out.GetUserAnswerPort;
import com.example.indexquiz.question.application.port.out.dto.DifficultQuestionResponses;
import com.example.indexquiz.useranswer.application.port.out.dto.SaveUserAnswersCommand;
import com.example.indexquiz.useranswer.domain.ScoreCount;
import com.example.indexquiz.useranswer.domain.ScoreCounts;
import com.example.indexquiz.useranswer.domain.UserAnswers;
import com.example.indexquiz.useranswer.domain.UserResult;
import com.example.indexquiz.useranswer.domain.VisitorProgress;
import com.example.indexquiz.useranswer.domain.VisitorQuestionCompletion;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAnswerPersistenceAdapter implements
        SaveUserAnswerPort,
        SaveUserResultPort,
        GetUserAnswerPort,
        GetDifficultQuestionPort,
        GetScoreDistributionPort,
        GetQuestionSetAveragesPort,
        SaveVisitorQuestionCompletionPort,
        GetVisitorProgressPort
{

    private final UserAnswerJpaRepository userAnswerJpaRepository;

    private final UserResultJpaRepository userResultJpaRepository;

    private final VisitorQuestionCompletionJpaRepository visitorQuestionCompletionJpaRepository;

    private final UserAnswerMapper userAnswerMapper;

    private final UserResultMapper userResultMapper;

    private final VisitorQuestionCompletionMapper visitorQuestionCompletionMapper;

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
    public ScoreCounts getScoreCounts(QuestionSet questionSet) {
        List<ScoreCount> scoreCounts = userResultJpaRepository.countByQuestionSetGroupByScore(questionSet);
        return new ScoreCounts(scoreCounts);
    }

    @Override
    public Optional<Double> getAverageScore(QuestionSet questionSet, int maxScore) {
        return userResultJpaRepository.findAverageByQuestionSetAndMaxScore(questionSet, maxScore);
    }

    @Override
    public void saveIfNotExists(VisitorQuestionCompletion completion) {
        try {
            VisitorQuestionCompletionEntity entity = visitorQuestionCompletionMapper.mapToEntity(completion);
            visitorQuestionCompletionJpaRepository.save(entity);
        } catch (DataIntegrityViolationException ignored) {
        }
    }

    @Override
    public VisitorProgress getByVisitorId(String visitorId) {
        Map<QuestionSet, Integer> bestScores = userResultJpaRepository
                .findBestScoresByVisitorId(visitorId)
                .stream()
                .collect(Collectors.toMap(
                        QuestionSetBestScore::getQuestionSet,
                        QuestionSetBestScore::getBestScore
                ));
        return new VisitorProgress(bestScores);
    }
}
