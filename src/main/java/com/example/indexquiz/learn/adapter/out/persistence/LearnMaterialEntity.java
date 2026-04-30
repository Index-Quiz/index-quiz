package com.example.indexquiz.learn.adapter.out.persistence;

import com.example.indexquiz.question.domain.QuestionSet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "learn_material")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LearnMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "question_set")
    private QuestionSet questionSet;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(name = "display_order")
    private int displayOrder;
}
