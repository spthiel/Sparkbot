package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.List;
import java.util.Stack;

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
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(User user, Guild guild) {
		return true;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
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

		Stack<String> openelements;
		for (int i = 0; i < str.size(); i++) {
			String s = str.get(i);
			if(s.contains(";")) {
				String[] splitted = s.split(";");

			}
		}

	}
}
