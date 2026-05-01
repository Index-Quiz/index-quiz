package com.example.indexquiz.useranswer.adapter.out.persistence;

import com.example.indexquiz.common.persistence.BaseTimeEntity;
import com.example.indexquiz.question.domain.QuestionSet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "visitor_question_completion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"visitor_id", "question_id"}),
        indexes = @Index(columnList = "visitor_id, question_set")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VisitorQuestionCompletionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "visitor_id")
    private String visitorId;

    @Column(name = "question_id")
    private long questionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "question_set")
    private QuestionSet questionSet;
}
