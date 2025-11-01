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
    public QuestionWithOptions getQuestionWithOptions(long questionId) {
        Question question = questionJpaRepository.findByQuestionOrder(questionId)
                .map(questionMapper::mapToQuestion)
                .orElseThrow(() -> new IndexQuizException(ErrorCode.QUESTION_NOT_FOUND));
        List<QuestionOption> options = questionOptionJpaRepository.findAllByQuestionId(question.getId())
                .stream()
                .map(questionOptionMapper::mapToQuestionOption)
                .toList();
        return new QuestionWithOptions(question, options);
    }
}
