package com.example.indexquiz.example.adapter.out.persistence.entity.answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "question_id")
    private long questionId;

    @NotNull
    @Column(name = "solution_id")
    private long solutionId;
}
