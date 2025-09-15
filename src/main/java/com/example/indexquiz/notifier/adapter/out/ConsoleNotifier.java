package com.example.indexquiz.notifier.adapter.out;

import com.example.indexquiz.notifier.application.port.out.Notifier;

public class ConsoleNotifier implements Notifier {

    @Override
    public void sendMessage(String message) {
        System.out.println("[메시지 발송] : " + message);
    }
}
