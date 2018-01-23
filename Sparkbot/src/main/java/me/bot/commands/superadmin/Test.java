package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public class Test implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Testcommand";
	}

	@Override
	public String[] getNames() {
		String[] names = {"test"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		String[] prefixes = {Prefixes.getSuperAdminPrefix()};
		return prefixes;
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {

	}

	@Override
	public void onLoad() {

	}
}
