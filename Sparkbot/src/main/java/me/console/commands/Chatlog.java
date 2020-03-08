package me.console.commands;

import discord4j.core.object.audit.ActionType;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;

import java.util.List;

import org.w3c.dom.Text;

import me.bot.base.Bot;
import me.main.utils.DiscordUtils;
import me.console.ConsoleCommand;

public class Chatlog implements ConsoleCommand {
	@Override
	public String[] getNames() {
		String[] out = {"chatlog"};
		return out;
	}

	@Override
	public String getHelp() {
		return "chatlog <bot> <guildid> <channelname> [amount]";
	}

	@Override
	public void run(String... args) {

		if(args.length >= 3)
			try {
				final int amount;
				if(args.length > 3) {
					amount = Integer.parseInt(args[3]);
				} else {
					amount = 100;
				}
				final long guildID = Long.parseLong(args[1]);
				Bot        bot     = Bot.getBotByName(args[0]);
				TextChannel channel = getChannel(bot, guildID, args[2]);

				if(channel == null) {
					System.err.println("Couldn't find that channel");
					return;
				}

				//TODO: Fix stuff
				
				channel.getGuild().subscribe(guild -> {
					System.out.println("Invites: ");
					guild.getAuditLog().subscribe(entry -> {
						System.out.println(entry.getActionType() + " " + entry.getResponsibleUserId() + " " + entry.getTargetId().orElse(null));
					});
				});
				
				System.out.println("Messages of #" + channel.getName() + " in " + channel.getGuild().block().getName());
                    Flux<Message> messages = channel.getMessagesBefore(channel.getLastMessageId().orElseThrow(RuntimeException::new)).take(amount);
                    messages.subscribe((result) -> {
                        String username;
                        if (result.getAuthor().isPresent()) {
                            username = result.getAuthor().get().getUsername();
                        } else {
                            username = "undef";
                        }
                        System.out.println("\t" + username + ": " + result
                                .getContent()
                                .orElse("none")
                                .replace("\n", "\n\t\t"));
                    });
			} catch(NumberFormatException e) {
				System.err.println("Please enter valid numbers.");
			}
		else
			System.err.println(getHelp());
	}

	private TextChannel getChannel(Bot bot, long guildID, String channelName) {
		Guild guild = bot.getClient().getGuildById(Snowflake.of(guildID)).block();
		for(Channel c : guild.getChannels().toIterable()) {
			if(c.getType().equals(Channel.Type.GUILD_TEXT)) {
				TextChannel channel = DiscordUtils.getTextChannelOfChannel(c);
				if (channel.getName().equalsIgnoreCase(channelName))
					return channel;
			}
		}

		return null;
	}

	private MessageChannel getChannel(Bot bot, long channelid) {
		return DiscordUtils.getMessageChannelOfChannel(bot.getClient().getChannelById(Snowflake.of(channelid)).block());
	}

	@Override
	public void onLoad() {

	}
}
