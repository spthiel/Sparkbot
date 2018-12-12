package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;

import me.bot.base.*;
import me.bot.base.polls.Bool;
import me.bot.base.polls.Input;
import me.bot.base.polls.Option;
import me.bot.base.polls.PollExitType;
import me.main.Prefixes;

import java.util.*;

public class Chars implements ICommand, IDisabledCommand {

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

	private static List<Permission> PERMISSIONS = Collections.singletonList(Permission.MANAGE_GUILD);

	@Override
	public List<Permission> getRequiredPermissions() {
		return PERMISSIONS;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, final Member author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		if (args.length >= 1) {

			if(guild == null || channel == null)
				return;

			switch (args[0]) {
				case "add":
					Input poll = new Input(bot, author, channel, "What Question would you like to add?", "Use `exit` to exit the Menu", false, 30000);
					bot.addPoll(poll);
					poll.subscribe((result,type) -> {
						if(type == PollExitType.SUCCESS)
							if (result != null && !result.equalsIgnoreCase("Stop")) {
								Bool skipable = new Bool(bot, author, channel, "Is this question optional?", "Use `exit` to exit the Menu or yes/no to answer it.", false, 30000);
								bot.addPoll(skipable);

								skipable.subscribe((result2,type2) -> {
									if(type2 != PollExitType.SUCCESS)
										return;
									HashMap<String,Object> toPut = new HashMap<>();
									toPut.put("q", result);
									toPut.put("s", result2);
									HashMap<String,Object> object = getConfig(bot,guild.getId().asLong());
									object.put("questions", toPut);
									write(bot,guild.getId().asLong(),object);
								});
							}
					});
					break;
				case "list":
					HashMap<String,Object> object = getConfig(bot,guild.getId().asLong());
					if (object.containsKey("questions")) {
						ArrayList<Object> questions = (ArrayList) object.get("questions");
						Option option = new Option(bot, author, channel, "List of your questions:", "Use `exit` to leave the Menu", false, -1);
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
					HashMap<String,Object> object2 = getConfig(bot,guild.getId().asLong());
					if (object2.containsKey("questions")) {
						ArrayList<Object> questions = (ArrayList) object2.get("questions");
						Option option = new Option(bot, author, channel, "List of your questions:", "Select the question you want to delete or use `exit` to leave the Menu", false, -1);
						List<String> qs = new ArrayList<>();
						for (Object q : questions) {
							String question = (String)((Map) q).get("q");
							qs.add(question);
							option.appendOption(question);
						}
						bot.addPoll(option);
						option.subscribe((result,type) -> {
							if(type == PollExitType.SUCCESS) {
								qs.remove(result);
								object2.put("questions", qs);
								write(bot,guild.getId().asLong(),object2);
							}
						});

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
	public void onLoad(final Bot bot) {
	}

	private HashMap<String,Object> getConfig(Bot bot,long guildid) {
		return bot.getResourceManager().getConfig("configs/" + guildid + "/rp", "settings.json");
	}

	private void write(Bot bot,long guildid,HashMap<String,Object> object) {
		bot.getResourceManager().writeConfig("configs/" + guildid + "/rp", "settings.json", object);
	}
}
