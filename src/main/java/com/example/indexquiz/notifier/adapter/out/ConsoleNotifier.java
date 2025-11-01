package com.example.indexquiz.notifier.adapter.out;

import com.example.indexquiz.notifier.application.port.out.NotifyPort;

public class ConsoleNotifier implements NotifyPort {

    @Override
    public void sendMessage(String message) {
        System.out.println("[메시지 발송] : " + message);
    }
}
