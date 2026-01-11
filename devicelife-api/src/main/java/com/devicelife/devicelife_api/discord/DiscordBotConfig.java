package com.devicelife.devicelife_api.discord;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Component
@RequiredArgsConstructor
public class DiscordBotConfig {

    @Value("${discord.bot-token:}")
    private String botToken;

    private final LifestyleDiscordCommandListener listener;

    private JDA jda;

    @PostConstruct
    public void start() throws Exception {
        if (botToken == null || botToken.isBlank()) {
            // 봇 안 쓸 거면 서버 기동은 되게 하고 싶으면 return 처리해도 됨
            throw new IllegalStateException("discord.bot-token 비어있음 (.env에 DISCORD_BOT_TOKEN 넣어)");
        }

        this.jda = JDABuilder.createDefault(botToken)
                .addEventListeners(listener)
                .build();

        // JDA 준비될 때까지 대충 기다림(서버 부팅 타이밍 안정화)
        this.jda.awaitReady();
    }
}
