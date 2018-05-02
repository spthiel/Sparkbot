package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.bot.base.polls.Bool;
import me.bot.base.polls.Input;
import me.bot.base.polls.Option;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.security.Permissions;
import java.util.*;

public class Chars implements ICommand {

	@Override
	public String[] getNames() {
		return new String[]{"character", "char"};
	}

	@Override
	public String getHelp() {
		return "character <add/remove/list>";
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
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
		if (args.length > 1) {

			if(guild == null || channel == null)
				return;

			switch (args[1]) {
				case "add":
					Thread t = new Thread(() -> {
						Input poll = new Input(bot, author, channel, "What Question would you like to add?", "Use `exit` to exit the Menu", "You left the menu", false, 30000);
						bot.addPoll(poll);
						String input = poll.get();
						if (input != null && !input.equalsIgnoreCase("stop")) {
							Bool skipable = new Bool(bot, author, channel, "What Question would you like to add?", "Use `exit` to exit the Menu", "You left the menu", false, 30000);
							bot.addPoll(skipable);
							boolean skip = skipable.get();

							Map<String,Object> toPut = new HashMap<>();
							toPut.put("q", input);
							toPut.put("s", skip);
							Map<String,Object> object = bot.getResourceManager().getConfig("configs/rp/" + guild.getId().asLong(), "settings.json");
							object.put("questions", toPut);
							bot.getResourceManager().writeConfig("configs/rp/" + guild.getId().asLong(), "settings.json", object);
						}
					});
					t.start();
					break;
				case "list":
					Map<String,Object> object = bot.getResourceManager().getConfig("configs/rp/" + guild.getId().asLong(), "settings.json");
					if (object.containsKey("questions")) {
						ArrayList<Object> questions = (ArrayList) object.get("questions");
						Option option = new Option(bot, author, channel, "List of your questions:", "Use `exit` to leave the Menu", "", false, -1);
						for (Object q : questions) {
							String question = (String)((Map) q).get("q");
							option.appendOption(question);
						}
						bot.addPoll(option);


					} else {
						MessageAPI.sendAndDeleteMessageLater(channel, "```\nYour server has no Character questions set yet\n```", 30000);
					}
					break;
				case "remove":
					Map<String,Object> object2 = bot.getResourceManager().getConfig("configs/rp/" + guild.getId().asLong(), "settings.json");
					if (object2.containsKey("questions")) {
						ArrayList<Object> questions = (ArrayList) object2.get("questions");
						Option option = new Option(bot, author, channel, "List of your questions:", "Select the question you want to delete or use `exit` to leave the Menu", "", false, -1);
						List<String> qs = new ArrayList<>();
						for (Object q : questions) {
							String question = (String)((Map) q).get("q");
							qs.add(question);
							option.appendOption(question);
						}
						bot.addPoll(option);
						Thread t2 = new Thread(() -> {
							qs.remove((int) option.get());
							object2.put("questions", qs);
							bot.getResourceManager().writeConfig("configs/rp/" + guild.getId().asLong(), "settings.json", object2);
						});

						t2.start();

					} else {
						MessageAPI.sendAndDeleteMessageLater(channel, "```\nYour server has no Character questions set yet\n```", 30000);
					}
					break;
				default:
					MessageAPI.sendAndDeleteMessageLater(channel, getHelp(), 10000);
			}

		} else {
			MessageAPI.sendAndDeleteMessageLater(channel, getHelp(), 10000);
		}
	}

	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
	public void onLoad() {
	}
}
