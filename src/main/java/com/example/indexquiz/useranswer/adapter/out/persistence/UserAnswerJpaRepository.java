package com.example.indexquiz.useranswer.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface UserAnswerJpaRepository extends JpaRepository<UserAnswerEntity, Long> {

    List<UserAnswerEntity> findAllBySubmitId(String submitId);

    //TODO 역정규화로 쿼리 최적화 시도
    @Query(value = """
            select t.questionId
            from(
                    select user_an.questionId, (100.0 * correct_table.correct_cnt / count(*)) as correct_percentage
                    from(
                select uan.questionId, count(*) as correct_cnt
                from UserAnswerEntity uan
                left join AnswerEntity as an on uan.questionId = an.questionId
                where uan.optionId = an.optionId
                group by uan.questionId ) as correct_table
                    right join UserAnswerEntity as user_an on user_an.questionId = correct_table.questionId
                    group by user_an.questionId
                ) as t
            order by t.correct_percentage
            limit :limit
            """,
            nativeQuery = true)
    List<Long> findDifficultQuestions(@Param(value = "limit") long limit);
}
