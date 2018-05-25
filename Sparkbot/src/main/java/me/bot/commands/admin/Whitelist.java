package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.*;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Whitelist implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
	public String getHelp() {
		return "Enables whitelist and adds channels too it.";
	}

	@Override
	public String[] getNames() {
		return new String[]{"whitelist"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getAdminPrefixesFor(guild);
	}

	private static Permission[] PERMISSIONS = new Permission[]{Permission.MANAGE_GUILD};

	@Override
	public Permission[] getRequiredPermissions() {
		return PERMISSIONS;
	}
	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
		Map<String,Object> map = getConfig(bot,guild.getId().asLong());
		List<String> whitelist;
		if(map.containsKey("channels")) {
			Object channels = map.get("channels");
			if(channels instanceof List && ((List) channels).size() != 0 && ((List) channels).get(0) instanceof String) {
				//noinspection unchecked
				whitelist = (List<String>)map.get("channels");
			} else
				whitelist = new ArrayList<>();
		} else {
			whitelist = new ArrayList<>();
		}
		String channelid = channel.getId().asString();
		MessageBuilder messageBuilder = new MessageBuilder();
		messageBuilder.withChannel(channel);
		if(whitelist.contains(channelid)) {
			whitelist.remove(channelid);
			if(whitelist.size() == 0) {
				messageBuilder.appendContent("You removed the channel <#" + channel.getId().asString() + "> from the whitelist and disabled the whitelist.");
			} else {
				messageBuilder.appendContent("You removed the channel <#" + channel.getId().asString() + "> from the whitelist.");
			}
		} else {

			whitelist.add(channelid);
			if(whitelist.size() == 1)
				messageBuilder.appendContent("You enabled the whitelist and added the channel <#" + channel.getId().asString() + "> to it.");
			else
				messageBuilder.appendContent("You added the channel <#" + channel.getId().asString() + "> to the whitelist.");

		}
		message.delete().subscribe();
		MessageAPI.sendAndDeleteMessageLater(messageBuilder,10000);
		map.put("channels",whitelist);
		write(bot,guild.getId().asLong(),map);
	}

	@Override
	public void onLoad() {

	}

	private Map<String,Object> getConfig(Bot bot, long guildid) {
		return bot.getResourceManager().getConfig("configs/" + guildid, "whitelist.json");
	}

	private void write(Bot bot,long guildid,Map<String,Object> object) {
		bot.getResourceManager().writeConfig("configs/" + guildid, "whitelist.json", object);
	}
}
