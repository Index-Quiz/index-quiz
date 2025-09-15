package com.example.indexquiz.useranswer.adapter.out.noti;

import com.example.indexquiz.notifier.application.port.out.NotifyPort;
import com.example.indexquiz.useranswer.application.port.out.SendUserResultMessagePort;
import com.example.indexquiz.useranswer.domain.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendUserResultMessageAdapter implements SendUserResultMessagePort {

    private final NotifyPort notifyPort;
    private final UserResultMessageResolver userResultMessageResolver;

    @Override
    public void sendUserResultMessage(UserResult userResult) {
        String message = userResultMessageResolver.resolveMessage(userResult);
        notifyPort.sendMessage(message);
    }
}
