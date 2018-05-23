package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.ResourceManager;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.security.Permissions;
import java.util.EnumSet;
import java.util.List;

public class SetUpdateChannel implements ICommand {
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

	@Override
	public boolean hasPermissions(User user, Guild guild) {
		EnumSet<Permission> perms = PermissionManager.getPermissions(guild,user).block();
		if(perms != null)
			return perms.contains(Permission.MANAGE_GUILD) || perms.contains(Permission.ADMINISTRATOR);
		else
			return false;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		ResourceManager manager = bot.getResourceManager();

	}

	@Override
	public void onLoad() {

	}
}
