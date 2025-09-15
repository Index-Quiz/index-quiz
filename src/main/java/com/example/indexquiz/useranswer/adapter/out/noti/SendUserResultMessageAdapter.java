package com.example.indexquiz.useranswer.adapter.out.noti;

import com.example.indexquiz.notifier.application.port.out.Notifier;
import com.example.indexquiz.useranswer.application.port.out.SendUserResultMessagePort;
import com.example.indexquiz.useranswer.domain.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendUserResultMessageAdapter implements SendUserResultMessagePort {

    private final Notifier notifier;
    private final UserResultMessageResolver userResultMessageResolver;

    @Override
    public void sendUserResultMessage(UserResult userResult) {
        String message = userResultMessageResolver.resolveMessage(userResult);
        notifier.sendMessage(message);
    }
}
