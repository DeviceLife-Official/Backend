package com.devicelife.devicelife_api.discord;


import com.devicelife.devicelife_api.domain.lifestyle.request.DeviceSearchDto;
import com.devicelife.devicelife_api.repository.lifestyle.DeviceSearchRepository;
import com.devicelife.devicelife_api.service.lifestyle.LifestyleFeaturedService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LifestyleDiscordCommandListener extends ListenerAdapter {

    private final LifestyleFeaturedService lifestyleFeaturedService;
    private final DeviceSearchRepository deviceSearchRepository;

    @Value("${discord.guild-id:}")
    private String guildId;

    @Value("${discord.admin-role-name:admin}")
    private String adminRoleName;

    @Override
    public void onReady(ReadyEvent event) {
        if (guildId == null || guildId.isBlank()) return;

        Guild guild = event.getJDA().getGuildById(guildId);
        if (guild == null) return;

        // 길드 커맨드로 등록(반영 빠름!)
        guild.upsertCommand("device-search", "디바이스 검색")
                .addOption(OptionType.STRING, "keyword", "검색어", true)
                .addOption(OptionType.INTEGER, "limit", "최대 개수(1~30, 기본 10)", false)
                .queue();

        guild.upsertCommand("featured-set", "태그별 대표 기기 3개 설정")
                .addOption(OptionType.STRING, "tag_key", "tagKey", true)
                .addOption(OptionType.INTEGER, "d1", "deviceId1", true)
                .addOption(OptionType.INTEGER, "d2", "deviceId2", true)
                .addOption(OptionType.INTEGER, "d3", "deviceId3", true)
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "device-search" -> handleDeviceSearch(event);
            case "featured-set" -> handleFeaturedSet(event);
        }
    }

    private void handleDeviceSearch(SlashCommandInteractionEvent event) {
        String keyword = event.getOption("keyword").getAsString();
        int limit = event.getOption("limit") == null ? 10 : (int) event.getOption("limit").getAsLong();
        int safeLimit = Math.min(Math.max(limit, 1), 30);

        List<DeviceSearchDto> list = deviceSearchRepository.searchByKeyword(keyword, safeLimit);

        if (list.isEmpty()) {
            event.reply("검색 결과 없음").setEphemeral(true).queue();
            return;
        }

        String msg = list.stream()
                .limit(10)
                .map(d -> String.format("- #%d %s", d.getDeviceId(), d.getModelName()))
                .collect(Collectors.joining("\n"));

        event.reply(msg).setEphemeral(true).queue();
    }

    private void handleFeaturedSet(SlashCommandInteractionEvent event) {
        if (!isAdmin(event.getMember())) {
            event.reply("권한 없음 (admin role 필요)").setEphemeral(true).queue();
            return;
        }

        String tagKey = event.getOption("tag_key").getAsString();
        long d1 = event.getOption("d1").getAsLong();
        long d2 = event.getOption("d2").getAsLong();
        long d3 = event.getOption("d3").getAsLong();

        lifestyleFeaturedService.setFeatured(tagKey, d1, d2, d3);
        event.reply("ok. 대표 기기 3개 바꿈").setEphemeral(true).queue();
    }

    private boolean isAdmin(Member member) {
        if (member == null) return false;
        if (adminRoleName == null || adminRoleName.isBlank()) return true; // 역할검증 끄고 싶으면 DISCORD_ADMIN_ROLE 비워
        return member.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase(adminRoleName));
    }
}
