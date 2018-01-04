package me.main;

import me.bot.base.configs.Config;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

	private static final String
			directory = "configs/main",
			filename = "botowner.txt"
	;

	private static Config config;

	public static List<Long> getBotAdmins() {

		List<Long> out = new ArrayList<>();

		config.getKeySet().forEach(key -> out.add(Long.parseLong(key)));

		return out;

	}

	public static boolean isBotAdmin(IUser user) {

		final long id = user.getLongID();

		return isBotAdmin(id);

	}

	public static boolean isBotAdmin(final long id) {

		if(updateConfigIfUnset()) {

			return config.hasKey(id + "");

		} else {
			return false;
		}

	}

	public static boolean addBotAdmin(IUser user) {

		final long id = user.getLongID();

		return addBotAdmin(id);

	}

	public static boolean addBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			config.addOrResetKey(id + "",false);
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public static boolean removeBotAdmin(IUser user) {

		final long id = user.getLongID();

		return removeBotAdmin(id);

	}

	public static boolean removeBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			config.removeKey(id + "");
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBotOwner(IUser user) {

		final long id = user.getLongID();

		return isBotOwner(id);

	}

	public static boolean isBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			return config.hasKey(id + "") && (boolean)config.getValue(id + "");

		} else {
			return false;
		}

	}

	public static boolean addBotOwner(IUser user) {

		final long id = user.getLongID();

		return addBotOwner(id);

	}

	public static boolean addBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			config.addOrResetKey(id + "",true);
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public static boolean removeBotOwner(IUser user) {

		final long id = user.getLongID();

		return removeBotOwner(id);

	}

	public static boolean removeBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			config.removeKey(id + "");
			updateConfig();
			return true;
		} else {
			return false;
		}
	}


	public static boolean updateConfigIfUnset() {
		if(config != null)
			return true;
		if(Main.getBot() == null)
			return false;

		config = Main.getBot().getResourceManager().getConfig(directory,filename);
		return true;
	}

	public static void updateConfig() {

		if(config == null)
			return;
		if(Main.getBot() == null)
			return;

		Main.getBot().getResourceManager().writeConfig(directory,filename,config);

	}

}
