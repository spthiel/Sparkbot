package me.main.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bot.base.Bot;

public class BotsOnDiscordUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BotsOnDiscordUtils.class);
	
	private static final String BASE_URL       = "https://bots.ondiscord.xyz/bot-api";
	private static final String GUILD_ENDPOINT = "/bots/$ID$/guilds";
	private static final String GUILD_PAYLOAD  = "{\n\"guildCount\":$COUNT$\n}";
	
	public static void updateGuildCount(Bot bot, final String key) {
		
		bot.getClient().getGuilds().count().subscribe(
				count -> {
					try {
						HashMap<String, String> header = new HashMap<>();
						header.put("Authorization", key);
						header.put("Content-Type","application/json");
						System.out.println("Sending guild count: " + count + " to Bots.onDiscord.xyz");
						HTTP.post(
								BASE_URL + GUILD_ENDPOINT.replace("$ID$", bot.getGateway().getSelfId().asString()),
								GUILD_PAYLOAD.replace("$COUNT$", count.toString()),
								header
								 );
					} catch (Exception e) {
						logger.error("Couldn't synchronize stats to BotsOnDiscord", e);
					}
				},
				Throwable:: printStackTrace
													 );
	}
}
