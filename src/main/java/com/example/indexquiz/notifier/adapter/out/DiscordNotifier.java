package com.example.indexquiz.notifier.adapter.out;

import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import com.example.indexquiz.notifier.config.DiscordProperties;
import com.example.indexquiz.notifier.application.port.out.NotifyPort;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordNotifier implements NotifyPort {

    private final DiscordProperties properties;

    private final JDA jda;

    public DiscordNotifier(DiscordProperties discordProperties) {
        this.properties = discordProperties;
        this.jda = initializeJda(properties.getToken());
    }

    private JDA initializeJda(String token) {
        try {
            return JDABuilder.createDefault(token).build().awaitReady();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IndexQuizException(ErrorCode.INITIALIZE_JDA_FAIL);
        }
    }

    @Override
    public void sendMessage(String message) {
        TextChannel channel = jda.getTextChannelById(properties.getChannelId());
        channel.sendMessage(message).queue();
    }
}
