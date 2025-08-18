package com.example.indexquiz.question.adapter.out.persistence;

import com.example.indexquiz.question.adapter.out.mapper.QuestionMapper;
import com.example.indexquiz.question.adapter.out.mapper.QuestionOptionMapper;
import com.example.indexquiz.question.application.port.out.GetQuestionPort;
import com.example.indexquiz.question.domain.Question;
import com.example.indexquiz.question.domain.QuestionOption;
import com.example.indexquiz.question.domain.QuestionWithOptions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPersistenceAdapter implements GetQuestionPort {

    private final QuestionJpaRepository questionJpaRepository;

    private final QuestionOptionJpaRepository questionOptionJpaRepository;

    private final QuestionMapper questionMapper;

    private final QuestionOptionMapper questionOptionMapper;

    @Override
    @Transactional(readOnly = true)
    public QuestionWithOptions getQuestionWithOptions(long questionId) {
        Question question = questionJpaRepository.findById(questionId)
                .map(questionMapper::mapToQuestion)
                .orElseThrow(IllegalStateException::new); // TODO: change to IndexQuestionException when #21 merge.
        List<QuestionOption> options = questionOptionJpaRepository.findAllByQuestionId(question.getId())
                .stream()
                .map(questionOptionMapper::mapToQuestionOption)
                .sorted(QuestionOption.getDefaultComparator())
                .toList();
        return new QuestionWithOptions(question, options);
    }
}
