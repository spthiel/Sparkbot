package me.bot.commands.admin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.ResourceManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

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
		String[] names = {"notify"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		return Prefixes.getAdminPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		EnumSet<Permissions> perms = user.getPermissionsForGuild(guild);
		return perms.contains(Permissions.ADMINISTRATOR) || perms.contains(Permissions.MANAGE_CHANNELS);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		ResourceManager manager = bot.getResourceManager();

	}

	@Override
	public void onLoad() {

	}
}
