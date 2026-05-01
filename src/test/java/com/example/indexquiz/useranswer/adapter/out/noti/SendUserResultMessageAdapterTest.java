package com.example.indexquiz.useranswer.adapter.out.noti;

import com.example.indexquiz.question.domain.QuestionSet;
import com.example.indexquiz.useranswer.domain.UserResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class SendUserResultMessageAdapterTest {

    @Autowired
    private SendUserResultMessageAdapter sendUserResultMessageAdapter;


    @Nested
    class SendUserResultMessage {

        @Test
        void 유저_성적을_발송한다() {
            UserResult userResult = new UserResult(10L, QuestionSet.A, 15, null);
            sendUserResultMessageAdapter.sendUserResultMessage(userResult);
        }
    }
}
