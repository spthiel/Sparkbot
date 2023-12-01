package me.bot.commands.moderation;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.IDisabledCommand;
import me.bot.base.configs.ResourceManager;
import me.main.Prefixes;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class SetUpdateChannel implements ICommand, IDisabledCommand {
	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
	public String getHelp() {
		return "Set's a channel to receive notifications on bot updates";
	}

	@Override
	public String[] getNames() {
		return new String[]{"notify"};
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
		ResourceManager manager = bot.getResourceManager();

	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
