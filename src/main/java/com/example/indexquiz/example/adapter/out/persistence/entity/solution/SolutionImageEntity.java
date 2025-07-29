package com.example.indexquiz.example.adapter.out.persistence.entity.solution;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solution_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "solution_id")
    private long solutionId;

    @NotBlank
    private String url;
}
