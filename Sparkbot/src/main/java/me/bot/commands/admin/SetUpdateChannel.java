package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.ResourceManager;
import me.main.PermissionManager;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

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
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		ResourceManager manager = bot.getResourceManager();

	}

	@Override
	public void onLoad() {

	}
}
