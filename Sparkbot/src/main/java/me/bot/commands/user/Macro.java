package me.bot.commands.user;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public class Macro implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Main command for Macromod related actions";
	}

	@Override
	public String[] getNames() {
		return new String[]{"macro","macromod"};
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return true;
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		if(args.length > 0) {
			switch(args[0]) {
				case "format":
					
			}
		}
	}

	@Override
	public void onLoad() {

	}

	private void format(List<String> str) {

	}
}
