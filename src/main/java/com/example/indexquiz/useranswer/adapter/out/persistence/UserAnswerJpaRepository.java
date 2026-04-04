package com.example.indexquiz.useranswer.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface UserAnswerJpaRepository extends JpaRepository<UserAnswerEntity, Long> {

    List<UserAnswerEntity> findAllBySubmitId(String submitId);

    //TODO 역정규화로 쿼리 최적화 시도
    @Query(value = """
            select t.question_id
            from(
                    select user_an.question_id, (100.0 * correct_table.correct_cnt / count(*)) as correct_percentage
                    from(
                select uan.question_id, count(*) as correct_cnt
                from user_answer as uan
                left join answer as an on uan.question_id = an.question_id
                where uan.option_id = an.option_id
                group by uan.question_id ) as correct_table
                    right join user_answer as user_an on user_an.question_id = correct_table.question_id
                    group by user_an.question_id
                ) as t
            order by t.correct_percentage
            limit :limit
            """,
            nativeQuery = true)
    List<Long> findDifficultQuestions(@Param(value = "limit") long limit);
}
