package me.bot.base.configs;


import com.fasterxml.jackson.core.JsonProcessingException;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import me.bot.base.Bot;
import me.main.Entry;

import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class PermissionManager {
	
	public static Mono<Boolean> hasGuildPermissions(Member author, List<Permission> perms) {
		if(perms == null) {
			return Mono.just(true);
		}
		
		return author.getBasePermissions()
				  .filter(permissions -> permissions.contains(Permission.ADMINISTRATOR) || permissions.containsAll(perms))
				  .hasElement();
	}
	
	private final Bot bot;

	public PermissionManager(Bot bot) {
		this.bot = bot;
	}
	
	@SuppressWarnings("SpellCheckingInspection")
	private static final String
			directory = "configs/main",
			filename = "botowner.json"
	;

	private HashMap<String,Object> config;

	public void setupPermissionFile() {

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

	public Entry<List<Snowflake>,List<Snowflake>> getBotAdmins() {

		if(updateConfigIfUnset()) {
			final List<Snowflake> owners = new ArrayList<>();
			final List<Snowflake> admins = new ArrayList<>();

			getBotAdminsList().forEach(object -> admins.add(Snowflake.of(object)));
			getBotOwnersList().forEach(object -> owners.add(Snowflake.of(object)));

			return new Entry<>(owners,admins);
		} else {
			return new Entry<>(new ArrayList<>(),new ArrayList<>());
		}

	}

	public boolean isBotAdmin(User user) {

		final long id = user.getId().asLong();

		return isBotAdmin(id);

	}

	public boolean isBotAdmin(final long id) {
		
		if (isBotOwner(id)) {
			return true;
		} else if(config.containsKey("Admins")) {
			return ((ArrayList<?>)config.get("Admins")).contains(id);
		} else {
			return false;
		}

	}

	public boolean addBotAdmin(User user) {

		final long id = user.getId().asLong();

		return addBotAdmin(id);

	}

	public boolean addBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			List<Long> admins;
			if(config.containsKey("Admins")) {
				if (config.get("Admins") instanceof List<?> ad) {
					if (!ad.isEmpty() && ad.get(0) instanceof Long) {
						//noinspection unchecked
						admins = (List<Long>) ad;
					} else {
						admins = new ArrayList<>();
					}
				} else {
					admins = new ArrayList<>();
				}
			} else {
				admins = new ArrayList<>();
			}
			admins.add(id);
			config.put("Admins",admins);
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeBotAdmin(User user) {

		final long id = user.getId().asLong();

		return removeBotAdmin(id);

	}

	public boolean removeBotAdmin(final long id) {

		if(updateConfigIfUnset()) {
			List<?> toPut = (ArrayList<?>)config.get("Admins");
			return removeIdFromList(id, toPut);
		} else {
			return false;
		}
	}
	
	private boolean removeIdFromList(long id, List<?> toPut) {
		
		for (int i = 0; i < toPut.size(); i++) {
			Object object = toPut.get(i);
			if (id == (Long) object) {
				toPut.remove(i);
				i--;
			}

		}
		updateConfig();
		return true;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isBotOwner(User user) {

		final long id = user.getId().asLong();

		return isBotOwner(id);

	}

	public boolean isBotOwner(final long id) {

		if(updateConfigIfUnset() && config.containsKey("Owners")) {
			return ((ArrayList<?>)config.get("Owners")).contains(id);
		} else {
			return false;
		}

	}

	public boolean addBotOwner(User user) {

		final long id = user.getId().asLong();

		return addBotOwner(id);

	}

	public boolean addBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			List<Long> owners = getBotOwnersList();
			owners.add(id);
			config.put("Owners",owners);
			System.out.println("added owner");
			updateConfig();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeBotOwner(User user) {

		final long id = user.getId().asLong();

		return removeBotOwner(id);

	}

	public boolean removeBotOwner(final long id) {

		if(updateConfigIfUnset()) {
			var toPut = (ArrayList<?>)config.get("Owners");
			return removeIdFromList(id, toPut);
		} else {
			return false;
		}
	}


	public boolean updateConfigIfUnset() {
		if(config != null) {
			return true;
		}
		
		try {
			config = bot.getResourceManager().getConfig(directory,filename);
		} catch (JsonProcessingException e) {
			Logger.getLogger("PermissionManager").throwing("PermissionManager", "updateConfigIfUnset", e);
			return false;
		}
		return true;
	}

	public void updateConfig() {

		if(config == null)
			return;

		bot.getResourceManager().writeConfig(directory,filename,config);

	}

	private List<Long> getBotAdminsList() {

		if(config.containsKey("Admins")) {
			if (config.get("Admins") instanceof List<?> ad) {
				if (!ad.isEmpty() && ad.get(0) instanceof Long) {
					//noinspection unchecked
					return (List<Long>) ad;
				} else {
					return new ArrayList<>();
				}
			} else {
				return new ArrayList<>();
			}
		} else {
			return new ArrayList<>();
		}
	}

	private List<Long> getBotOwnersList() {

		if(config.containsKey("Owners")) {
			if (config.get("Owners") instanceof List<?> ow) {
				if (!ow.isEmpty() && ow.get(0) instanceof Long) {
					//noinspection unchecked
					return (List<Long>) ow;
				} else {
					return new ArrayList<>();
				}
			} else {
				return new ArrayList<>();
			}
		} else {
			return new ArrayList<>();
		}
	}

}
