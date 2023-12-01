package me.bot.commands.moderation;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.bot.base.*;
import me.main.Prefixes;

@SuppressWarnings("unused")
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

	private static final List<Permission> PERMISSIONS = Collections.singletonList(Permission.MANAGE_GUILD);

	@Override
	public List<Permission> getRequiredPermissions() {
		return PERMISSIONS;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		HashMap<String,Object> map = getConfig(bot,guild.getId().asLong());
		List<String> whitelist;
		if(map.containsKey("channels")) {
			Object channels = map.get("channels");
			if(channels instanceof List<?> channelList && !channelList.isEmpty() && channelList.get(0) instanceof String) {
				//noinspection unchecked
				whitelist = (List<String>)map.get("channels");
			} else
				whitelist = new ArrayList<>();
		} else {
			whitelist = new ArrayList<>();
		}
		String channelId = channel.getId().asString();
		MessageBuilder messageBuilder = new MessageBuilder();
		messageBuilder.withChannel(channel);
		if(whitelist.contains(channelId)) {
			whitelist.remove(channelId);
			if(whitelist.isEmpty()) {
				messageBuilder.appendContent("You removed the channel <#" + channel.getId().asString() + "> from the whitelist and disabled the whitelist.");
			} else {
				messageBuilder.appendContent("You removed the channel <#" + channel.getId().asString() + "> from the whitelist.");
			}
		} else {

			whitelist.add(channelId);
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
	public void onLoad(final Bot bot) {

	}

	private HashMap<String,Object> getConfig(Bot bot, long guildId) {
		return bot.getResourceManager().getConfig("configs/" + guildId, "whitelist.json");
	}

	private void write(Bot bot,long guildId,HashMap<String,Object> object) {
		bot.getResourceManager().writeConfig("configs/" + guildId, "whitelist.json", object);
	}
}
