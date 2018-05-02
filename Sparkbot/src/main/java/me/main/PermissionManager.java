package me.main;


import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class PermissionManager {

	private static final String
			directory = "configs/main",
			filename = "botowner.json"
	;

	private static Map<String,Object> config;

	public static Mono<EnumSet<Permission>> getPermissions(long guildId, long userId) {
		return Main.getBot().getClient().getMemberById(Snowflake.of(guildId), Snowflake.of(userId))
				.flatMapMany(Member::getRoles)
				.map(Role::getPermissions)
				.map(PermissionSet::asEnumSet)
				.reduce((set0, set1) -> {
					EnumSet<Permission> copy = EnumSet.copyOf(set0);
					copy.addAll(set1);
					return copy;
				});
	}

	public static Mono<EnumSet<Permission>> getPermissions(Guild guild, User user) {
		return Main.getBot().getClient().getMemberById(guild.getId(), user.getId())
				.flatMapMany(Member::getRoles)
				.map(Role::getPermissions)
				.map(PermissionSet::asEnumSet)
				.reduce((set0, set1) -> {
					EnumSet<Permission> copy = EnumSet.copyOf(set0);
					copy.addAll(set1);
					return copy;
				});
	}

	public static void setupPermfile() {

		if(updateConfigIfUnset()) {

			if (!config.containsKey("Admins")) {
				config.put("Admins",new ArrayList<Long>());
			}

			if (!config.containsKey("Owners")) {
				config.put("Owners",new ArrayList<Long>());
			}

			updateConfig();
		}
	}

	public static List<Long> getBotAdmins() {

		if(updateConfigIfUnset()) {
			final List<Long> out = new ArrayList<>();

			((ArrayList) config.get("Owners")).forEach(object -> out.add((Long) object));
			((ArrayList) config.get("Admins")).forEach(object -> out.add((Long) object));

			return out;
		} else {
			return null;
		}

	}

	public static boolean isBotAdmin(User user) {

		final long id = user.getId().asLong();

		return isBotAdmin(id);

	}

	public static boolean isBotAdmin(final long id) {

		if(updateConfigIfUnset() && config.containsKey("Admins")) {
			return ((ArrayList)config.get("Admins")).contains(id) || isBotOwner(id);
		} else {
			return false;
		}

	}

	public static boolean addBotAdmin(User user) {

		final long id = user.getId().asLong();

		return addBotAdmin(id);

	}

	public static boolean addBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			((ArrayList)config.get("Admins")).add(id);
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public static boolean removeBotAdmin(User user) {

		final long id = user.getId().asLong();

		return removeBotAdmin(id);

	}

	public static boolean removeBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			List<Object> toPut = (ArrayList)config.get("Admins");
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

	public static boolean isBotOwner(User user) {

		final long id = user.getId().asLong();

		return isBotOwner(id);

	}

	public static boolean isBotOwner(final long id) {

		if(updateConfigIfUnset() && config.containsKey("Owners")) {
			return ((ArrayList)config.get("Owners")).contains(id);
		} else {
			return false;
		}

	}

	public static boolean addBotOwner(User user) {

		final long id = user.getId().asLong();

		return addBotOwner(id);

	}

	public static boolean addBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			((ArrayList)config.get("Owners")).add(id);
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public static boolean removeBotOwner(User user) {

		final long id = user.getId().asLong();

		return removeBotOwner(id);

	}

	public static boolean removeBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			System.out.println(config.get("Owners").getClass().getCanonicalName());
			List<Object> toPut = (ArrayList)config.get("Owners");
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
