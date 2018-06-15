package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.utils.DiscordUtils;
import me.bot.gifs.Gifmanager;
import me.main.Prefixes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Gif implements ICommand {

	private List<Gifmanager> gifmanager;
	private String commands = "gif";

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
			DiscordUtils.getMember(bot, guild, id).subscribe(
					member -> {
						Gifmanager manager = getGifmanager(command);
						if (manager != null)
							manager.run(channel, authorname, member.getNickname().orElse(member.getDisplayName()));
					}
			);
		} else if (membername.matches("\\d{8,}")) {
			DiscordUtils.getMember(bot, guild, membername).subscribe(
					member -> {
						Gifmanager manager = getGifmanager(command);
						if (manager != null)
							manager.run(channel, authorname, member.getNickname().orElse(member.getDisplayName()));
					}
			);
		} else {
			Gifmanager manager = getGifmanager(command);
			if (manager != null)
				manager.run(channel, authorname, membername);
		}

	}

	private void sendHelp(TextChannel channel) {

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
		gifmanager = new ArrayList<>();

		addGifmanager(new Gifmanager("wave",new String[]{"https://media1.tenor.com/images/943a3f95936d66dc0c78fd445893431e/tenor.gif?itemid=9060940"},"%executor waves at %user"));
		addGifmanager(new Gifmanager("hug",new String[]{
				"https://media3.giphy.com/media/3M4NpbLCTxBqU/giphy.gif",
				"https://media.tenor.com/images/7db5f172665f5a64c1a5ebe0fd4cfec8/tenor.gif"},"%executor hugs %user"));
		addGifmanager(new Gifmanager("blush",new String[]{"http://66.media.tumblr.com/01d11c6bcf340db8bc307f1beeb2f8fb/tumblr_ogsqu35lLv1vemg2qo1_500.gif"},"%executor blushes"));
		addGifmanager(new Gifmanager("angry",new String[]{"https://media.tenor.com/images/ad5544c0cc1b04622cc3413ddd1fe63a/tenor.gif"},"%executor is angry"));
	}

	private void addGifmanager(Gifmanager manager) {
		gifmanager.add(manager);
		commands += "|" + manager.getName();
	}
}
