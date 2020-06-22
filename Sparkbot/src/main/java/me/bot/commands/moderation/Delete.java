package me.bot.commands.moderation;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.Prefixes;

public class Delete implements ICommand {

	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
	public String getHelp() {
		return "Deletes messages in bulk";
	}

	@Override
	public String[] getNames() {
		return new String[]{"clear", "prune"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getAdminPrefixesFor(guild);
	}

	private static List<Permission> PERMISSIONS = Collections.singletonList(Permission.MANAGE_MESSAGES);

	@Override
	public List<Permission> getRequiredPermissions() {
		return PERMISSIONS;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		List<Permission> out = new ArrayList<>();
		out.add(Permission.MANAGE_MESSAGES);
		return out;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {

		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("channel")) {

				System.out.println("works");
				final int[] amount = {0};
				message.delete().subscribe();
				Flux<Snowflake> messages = channel.getMessagesBefore(message.getId()).map(Message::getId);
				messages.count().subscribe((result) -> {
					amount[0] += result;
					channel.bulkDelete(messages).subscribe(
							ignored -> amount[0]--,
							Throwable::printStackTrace,
							() -> MessageAPI.sendAndDeleteMessageLater(channel, ":white_check_mark: **| Successfully deleted " + (amount[0]) + " messages from <#" + channel.getId().asLong() + "> **", 10000L)
					);
				});


			} else {

				try {
					int num = Integer.parseInt(args[0]);
					if (num > 1000)
						num = 1000;
					final int[] amount = {num};
					message.delete().subscribe();
					channel.bulkDelete(channel.getMessagesBefore(message.getId()).take(num).map(Message::getId)).subscribe((ignored) -> amount[0]--);
					MessageAPI.sendAndDeleteMessageLater(channel, ":white_check_mark: **| Successfully deleted " + (amount[0]) + " messages from <#" + channel.getId().asLong() + "> **", 10000L);
				} catch (NumberFormatException e) {
					MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **|" + args[0] + " is not a valid number.**", 5000L);
				}
			}
		} else {
			MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| Please enter the amount of messages you want to delete.**\nExample: `" + Prefixes.getAdminPrefixFor(guild) + "clear 10`", 5000L);
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
