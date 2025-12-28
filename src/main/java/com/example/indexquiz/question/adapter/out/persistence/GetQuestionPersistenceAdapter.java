package com.example.indexquiz.question.adapter.out.persistence;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.question.adapter.out.mapper.QuestionMapper;
import com.example.indexquiz.question.adapter.out.mapper.QuestionOptionMapper;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetQuestionPersistenceAdapter implements GetQuestionPort {

    private final QuestionJpaRepository questionJpaRepository;

    private final QuestionOptionJpaRepository questionOptionJpaRepository;

    private final QuestionMapper questionMapper;

    private final QuestionOptionMapper questionOptionMapper;

    @Override
    public QuestionWithOptions getQuestionWithOptions(long questionOrder) {
        Question question = questionJpaRepository.findByQuestionOrder(questionOrder)
                .map(questionMapper::mapToQuestion)
                .orElseThrow(() -> new IndexQuizException(ErrorCode.QUESTION_NOT_FOUND));
        List<QuestionOption> options = questionOptionJpaRepository.findAllByQuestionId(question.getId())
                .stream()
                .map(questionOptionMapper::mapToQuestionOption)
                .toList();
        return new QuestionWithOptions(question, options);
    }

    @Override
    public List<QuestionWithOptions> getAllQuestionWithOptions(List<Long> questionIds) {
        List<Question> questions = questionJpaRepository.findAllById(questionIds).stream()
                .map(questionMapper::mapToQuestion)
                .toList();

        Map<Long, List<QuestionOption>> optionByQuestions = questionOptionJpaRepository.findAllByQuestionIds(questionIds)
                .stream()
                .map(questionOptionMapper::mapToQuestionOption)
                .collect(Collectors.groupingBy(QuestionOption::getQuestionId));
        return resolveQuestionWithOptions(questions, optionByQuestions);
    }

    private List<QuestionWithOptions> resolveQuestionWithOptions(List<Question> questions, Map<Long, List<QuestionOption>> optionByQuestions) {
        return questions.stream()
                .map(question -> new QuestionWithOptions(question, optionByQuestions.getOrDefault(question.getId(), List.of())))
                .toList();
    }
}
