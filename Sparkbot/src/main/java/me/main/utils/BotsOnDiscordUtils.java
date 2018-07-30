package me.main.utils;

import java.util.HashMap;

import me.bot.base.Bot;

public class BotsOnDiscordUtils {
	
	private static String BASE_URL = "https://bots.ondiscord.xyz/bot-api",
			GUILD_ENDPOINT         = "/bots/$ID$/guilds",
			GUILD_PAYLOAD          = "{\n\"guildCount\":$COUNT$\n}";
	
	public static void updateGuildCount(Bot bot, final String key) {
		
		bot.getClient().getGuilds().count().subscribe(
				count -> {
					try {
						HashMap<String, String> header = new HashMap<>();
						header.put("Authorization", key);
						header.put("Content-Type","application/json");
						System.out.println("Sending guidcount: " + count + " to Bots.onDiscord.xyz");
						HTTP.post(
								BASE_URL + GUILD_ENDPOINT.replace("$ID$", bot.getBotuser().getId().asString()),
								GUILD_PAYLOAD.replace("$COUNT$", count.toString()),
								header
								 );
					} catch (Exception e) {
						e.printStackTrace();
					}
				},
				Throwable:: printStackTrace
													 );
	}
}
