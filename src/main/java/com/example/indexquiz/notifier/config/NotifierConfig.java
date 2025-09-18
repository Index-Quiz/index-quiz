package com.example.indexquiz.notifier.config;

import com.example.indexquiz.notifier.adapter.out.ConsoleNotifier;
import com.example.indexquiz.notifier.adapter.out.DiscordNotifier;
import com.example.indexquiz.notifier.application.port.out.NotifyPort;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NotifierConfig {

    @Profile({"dev", "prod"})
    @Configuration
    @RequiredArgsConstructor
    @EnableConfigurationProperties(DiscordProperties.class)
    public static class DiscordNotifierConfig {

        private final DiscordProperties discordProperties;

        @Bean
        public NotifyPort discordNotifier() {
            return new DiscordNotifier(discordProperties);
        }
    }

    @Profile({"test", "local"})
    @Configuration
    public static class ConsoleNotifierConfig {

        @Bean
        public NotifyPort consoleNotifier() {
            return new ConsoleNotifier();
        }
    }
}
