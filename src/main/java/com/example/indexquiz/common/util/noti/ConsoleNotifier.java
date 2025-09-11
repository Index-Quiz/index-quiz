package com.example.indexquiz.common.util.noti;

public class ConsoleNotifier implements Notifier {

    @Override
    public void sendMessage(String message) {
        System.out.println("[메시지 발송] : " + message);
    }
}
