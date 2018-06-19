package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.bot.base.utils.DiscordUtils;
import me.bot.gifs.Gifmanager;
import me.main.Prefixes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gif implements ICommand {

	private static Gif instance;
	private List<Gifmanager> gifmanager;
	private String commands = "gif";

	public Gif() {
		instance = this;
	}

	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Returns gifs";
	}

	@Override
	public String[] getNames() {
		return commands.split("\\|");
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
		if(commandname.equalsIgnoreCase("gif")) {
			if(args.length > 0) {
				parseCommand(bot, guild, channel, author, args[0], (args.length > 1 ? args[1] : null));
			} else {
				sendHelp(channel);
			}
		} else {
			parseCommand(bot, guild, channel, author, commandname, (args.length > 0 ? args[0] : null));
		}
	}

	private void parseCommand(Bot bot, Guild guild, TextChannel channel, Member author, String command, @Nullable String arg) {
		String authorname = author.getNickname().orElse(author.getDisplayName());
		String membername = (arg != null ? arg : "me");

		if (membername.matches("<@(\\d{8,})>")) {
			String id = membername.replaceAll("<@(\\d+)>", "$1");
			if(id.equalsIgnoreCase(bot.getBotuser().getId().asString())) {
				Gifmanager manager = getGifmanager(command);
				if (manager != null)
					manager.run(channel, authorname, "me");
			} else {
				DiscordUtils.getMember(bot, guild, id).subscribe(
						member -> {
							Gifmanager manager = getGifmanager(command);
							if (manager != null)
								manager.run(channel, authorname, member.getNickname().orElse(member.getDisplayName()));
						}
				);
			}

		} else if (membername.matches("\\d{8,}")) {
			if(membername.equalsIgnoreCase(bot.getBotuser().getId().asString())) {
				Gifmanager manager = getGifmanager(command);
				if (manager != null)
					manager.run(channel, authorname, "me");
			} else {
				DiscordUtils.getMember(bot, guild, membername).subscribe(
						member -> {
							Gifmanager manager = getGifmanager(command);
							if (manager != null)
								manager.run(channel, authorname, member.getNickname().orElse(member.getDisplayName()));
						}
				);
			}
		} else {
			DiscordUtils.getMember(bot, guild, membername).subscribe(
					member -> {
						Gifmanager manager = getGifmanager(command);
						if (manager != null) {
							String name;
							if(member.getId().equals(bot.getBotuser().getId()))
								name = "me";
							else
								name = member.getNickname().orElse(member.getDisplayName());

							manager.run(channel, authorname, name);
						}
					}
			);
		}

	}

	private void sendHelp(TextChannel channel) {
		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel);
		builder.appendContent("Ugly help page for gif: \n");
		builder.appendContent("`s!gif <option> <user>` or `s!<option> <user>`\n");
		builder.appendContent("Options are:\n");
		gifmanager.forEach(manager -> builder.appendContent(" `" + manager.getName() + "` "));
		builder.send().subscribe();
	}

	private Gifmanager getGifmanager(String name) {
		for (Gifmanager manager : gifmanager) {
			if(manager.getName().equalsIgnoreCase(name))
				return manager;
		}
		return null;
	}

	@Override
	public void onLoad(Bot bot) {

		Map<String,Object> config = bot.getResourceManager().getConfig("configs/main","gifs.json");
		if(config.keySet().isEmpty())
			bot.getResourceManager().writeConfig("configs/main","gifs.json",config);

		loadGiffmanager(bot);

	}

	public void loadGiffmanager(Bot bot) {

		gifmanager = new ArrayList<>();

		Map<String,Object> config = bot.getResourceManager().getConfig("configs/main","gifs.json");

		for(String key : config.keySet()) {
			try {

				Map<String,Object> entry = (Map<String, Object>) config.get(key);

				String replacer = (String)entry.get("repl");

				ArrayList<String> gifs = (ArrayList<String>) entry.get("gifs");

				addGifmanager(new Gifmanager(key, gifs.toArray(new String[gifs.size()]), replacer));
			} catch(Exception ignored) {

			}
		}
	}

	private void addGifmanager(Gifmanager manager) {
		gifmanager.add(manager);
		commands += "|" + manager.getName();
	}

	public static Gif getInstance() {
		return instance;
	}
}
