package me.main;

import org.json.JSONObject;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

	private static final String
			directory = "configs/main",
			filename = "botowner.json"
	;

	private static JSONObject config;

	public static List<Long> getBotAdmins() {

		final List<Long> out = new ArrayList<>();


		config.getJSONArray("Owners").forEach(object -> out.add((Long)object));
		config.getJSONArray("Admins").forEach(object -> out.add((Long)object));

		return out;

	}

	public static boolean isBotAdmin(IUser user) {

		final long id = user.getLongID();

		return isBotAdmin(id);

	}

	public static boolean isBotAdmin(final long id) {

		if(updateConfigIfUnset() && config.has("Admins")) {

			return config.getJSONArray("Admins").toList().contains(id) || isBotOwner(id);
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
			config.getJSONArray("Admins").put(id);
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
			List<Object> toPut = config.getJSONArray("Admins").toList();
			for (int i = 0; i < toPut.size(); i++) {
				Object object = toPut.get(i);
				if (id == (Long) object) {
					toPut.remove(i);
				}

			}
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

		if(updateConfigIfUnset() && config.has("Owners")) {
			return config.getJSONArray("Owners").toList().contains(id);
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
			config.getJSONArray("Owners").put(id);
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
			List<Object> toPut = config.getJSONArray("Owners").toList();
			for (int i = 0; i < toPut.size(); i++) {
				Object object = toPut.get(i);
				if (id == (Long) object) {
					toPut.remove(i);
				}

			}
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
