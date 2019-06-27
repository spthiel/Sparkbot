package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import reactor.util.function.Tuples;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Emoji implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Enlarges an emoji";
	}

	@Override
	public String[] getNames() {
		return new String[]{"emoji","enlarge"};
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

	private static Pattern pattern = Pattern.compile("<a?:(.+?):(\\d+)>");

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
		if(args.length >= 1) {
			String emoji = args[0];
			if(emoji.matches("<:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<:.+?:(\\d+)>","$1");
				String finalEmoji = emoji;
				channel.createMessage(s -> s.setEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji + ".png?v=1"))).subscribe();
			} else if(emoji.matches("<a:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<a:.+?:(\\d+)>","$1");
				String finalEmoji1 = emoji;
				channel.createMessage(s -> s.setEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji1 + ".gif?v=1"))).subscribe();
			} else if(emoji.matches("a\\d{8,}")) {
				String finalEmoji2 = emoji;
				channel.createMessage(s -> s.setEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji2.replace("a","") + ".gif?v=1"))).subscribe();
			} else if(emoji.matches("\\d{8,}")) {
				String finalEmoji3 = emoji;
				channel.createMessage(s -> s.setEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji3 + ".png?v=1"))).subscribe();
			} else {
				List<String> argsList = Arrays.asList(args);
				int ofMessageIndex = argsList.indexOf("of");
				int inChannelIndex = argsList.indexOf("in");

				System.out.println("Index: " + ofMessageIndex + " " + inChannelIndex);

				if(ofMessageIndex < 0)
					return;
				if(args.length < ofMessageIndex+1)
					return;

				String messageID = args[ofMessageIndex+1];
				String channelID = (inChannelIndex >= 0 && args.length > inChannelIndex+1 ? args[inChannelIndex+1] : null);

				System.out.println("ID: " + messageID + " " + channelID);

				if(channelID != null && channelID.matches("<#(\\d{8,})>"))
					channelID = channelID.replaceAll("<#(\\d{8,})>","$1");

				Snowflake messageFlake = Snowflake.of(messageID);
				Snowflake channelFlake = (channelID != null ? Snowflake.of(channelID) : channel.getId());

				bot.getClient().getMessageById(channelFlake,messageFlake).subscribe(
					message1 -> message1.getAuthor().ifPresent(
						messageAuthor -> {
							String c = message1.getContent().orElse("");
							Matcher m = pattern.matcher(c);

							Consumer<EmbedCreateSpec> embed = e -> {
								e.setTitle("Emojis of " + messageAuthor.getUsername() + "'s message")
										.setColor(new Color(0xdc143c));

								int counter = 1;
								while (m.find()) {
									e.addField(" - " + counter + ". " + m.group(1),m.group(2),false);
									counter++;
								}
							};
							channel.createMessage(spec -> spec.setEmbed(embed)).subscribe();
						}
					),
					error -> channel.createMessage(spec -> spec.setContent("An error occured whilst getting the message.")).subscribe()
				);
			}
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
