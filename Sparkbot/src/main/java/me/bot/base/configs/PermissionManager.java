package me.bot.base.configs;


import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import discord4j.core.object.util.Snowflake;
import me.bot.base.Bot;
import me.bot.base.configs.ResourceManager;
import me.main.Entry;
import me.main.Main;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class PermissionManager {

	private Bot bot;

	public PermissionManager(Bot bot) {
		this.bot = bot;
	}

	private static final String
			directory = "configs/main",
			filename = "botowner.json"
	;

	private Map<String,Object> config;

	public Mono<EnumSet<Permission>> getPermissions(long guildId, long userId) {
		return bot.getClient().getMemberById(Snowflake.of(guildId), Snowflake.of(userId))
				.flatMapMany(Member::getRoles)
				.map(Role::getPermissions)
				.map(PermissionSet::asEnumSet)
				.reduce((set0, set1) -> {
					EnumSet<Permission> copy = EnumSet.copyOf(set0);
					copy.addAll(set1);
					return copy;
				});
	}

	public Mono<EnumSet<Permission>> getPermissions(Guild guild, User user) {
		return bot.getClient().getMemberById(guild.getId(), user.getId())
				.flatMapMany(Member::getRoles)
				.map(Role::getPermissions)
				.map(PermissionSet::asEnumSet)
				.reduce((set0, set1) -> {
					EnumSet<Permission> copy = EnumSet.copyOf(set0);
					copy.addAll(set1);
					return copy;
				});
	}

	public void setupPermfile() {

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

			getBotAdminsList().forEach(object -> admins.add(Snowflake.of((Long) object)));
			getBotOwnersList().forEach(object -> owners.add(Snowflake.of((Long) object)));

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

		if(updateConfigIfUnset() && config.containsKey("Admins")) {
			return ((ArrayList)config.get("Admins")).contains(id) || isBotOwner(id);
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
				if (config.get("Admins") instanceof List) {
					List ad = ((List) config.get("Admins"));
					if (ad.size() > 0 && ad.get(0) instanceof Long) {
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

	public boolean isBotOwner(User user) {

		final long id = user.getId().asLong();

		return isBotOwner(id);

	}

	public boolean isBotOwner(final long id) {

		if(updateConfigIfUnset() && config.containsKey("Owners")) {
			return ((ArrayList)config.get("Owners")).contains(id);
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


	public boolean updateConfigIfUnset() {
		if(config != null) {
			return true;
		}

		config = bot.getResourceManager().getConfig(directory,filename);
		return true;
	}

	public void updateConfig() {

		if(config == null)
			return;

		bot.getResourceManager().writeConfig(directory,filename,config);

	}

	private List<Long> getBotAdminsList() {

		if(config.containsKey("Admins")) {
			if (config.get("Admins") instanceof List) {
				List ad = ((List) config.get("Admins"));
				if (ad.size() > 0 && ad.get(0) instanceof Long) {
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
			if (config.get("Owners") instanceof List) {
				List ow = ((List) config.get("Owners"));
				if (ow.size() > 0 && ow.get(0) instanceof Long) {
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
