package me.bot.commands.user;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
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

	private static final Pattern pattern = Pattern.compile("<a?:(.+?):(\\d+)>");

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		if(args.length >= 1) {
			String emoji = args[0];
			if(emoji.matches("<:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<:.+?:(\\d+)>","$1");
				final String finalEmoji = emoji;
				channel.createMessage(s -> s.addEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji + ".png?v=1"))).subscribe();
			} else if(emoji.matches("<a:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<a:.+?:(\\d+)>","$1");
				final String finalEmoji = emoji;
				channel.createMessage(s -> s.addEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + finalEmoji + ".gif?v=1"))).subscribe();
			} else if(emoji.matches("a\\d{8,}")) {
				channel.createMessage(s -> s.addEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + args[0].replace("a","") + ".gif?v=1"))).subscribe();
			} else if(emoji.matches("\\d{8,}")) {
				channel
						.createMessage(s -> s.addEmbed(spec -> spec.setImage("https://cdn.discordapp.com/emojis/" + args[0] + ".png?v=1")))
						.subscribe();
			} else if(args.length == 1) {
				final String finalEmoji = emoji;
				guild.getEmojis()
					 .filter(e -> e.getName().equalsIgnoreCase(finalEmoji))
					 .map(GuildEmoji :: getImageUrl)
					 .take(1)
					 .switchIfEmpty(Mono.just(""))
					 .subscribe(emojiURL -> {
					 	if(emojiURL.isEmpty()) {
					 		return;
						}
						 channel.createMessage(s -> s.addEmbed(spec -> spec.setImage(emojiURL))).subscribe();
					 });
			} else {
				List<String> argsList = Arrays.asList(args);
				int ofMessageIndex = argsList.indexOf("of");
				int inChannelIndex = argsList.indexOf("in");

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

				bot.getGateway().getMessageById(channelFlake,messageFlake).subscribe(
					message1 -> message1.getAuthor().ifPresent(
						messageAuthor -> {
							String c = message1.getContent();
							Matcher m = pattern.matcher(c);

							EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder();
							embed
								.title("Emojis of " + messageAuthor.getUsername() + "'s message")
								.color(Color.of(0xdc143c));

								int counter = 1;
								while (m.find()) {
									embed.addField(" - " + counter + ". " + m.group(1),m.group(2),false);
									counter++;
								}
							channel.createMessage(embed.build()).subscribe();
						}
					),
					error -> channel.createMessage("An error occurred whilst getting the message.").subscribe()
				);
			}
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
