package me.bot.base;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

public class DiscordUtils {

	private Bot bot;

	public DiscordUtils(Bot bot) {
		this.bot = bot;
	}

	public IUser getUserByID(final long id) {
		return RequestBuffer.request(() -> {
			return bot.getClient().getUserByID(id);
		}).get();
	}

}
