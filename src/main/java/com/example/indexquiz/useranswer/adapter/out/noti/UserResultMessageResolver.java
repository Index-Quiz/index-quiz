package com.example.indexquiz.useranswer.adapter.out.noti;

import com.example.indexquiz.useranswer.domain.UserResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class UserResultMessageResolver {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String USER_RESULT_MESSAGE_FORMAT = """
            # 📊 퀴즈 결과
            
            👥 현재까지 푼 사람 수 : **%d명**
            
            📚 풀이 문제 세트 : `%s`
            
            🏆 점수 : **%d / %d**
            
            🕒 제출 시각 : %s
            """;

    public String resolveMessage(UserResult userResult) {
        long solvedCount = userResult.getId();
        String questionSetName = userResult.getQuestionSet().name();
        int score = userResult.getScore();
        int total = userResult.getQuestionSet().getQuestionCount();
        String submittedAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        return USER_RESULT_MESSAGE_FORMAT.formatted(solvedCount, questionSetName, score, total, submittedAt);
    }
}
