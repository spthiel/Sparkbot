package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.bot.base.configs.HTTP;
import me.macro.MacroFormatter;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

import java.util.*;

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

	private static Permission[] PERMISSIONS = new Permission[]{};

	@Override
	public Permission[] getRequiredPermissions() {
		return PERMISSIONS;
	}
	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {

		if (args.length >= 1) {
			switch (args[0]) {
				case "format":
					if (message.getAttachments().isEmpty() && args.length > 1) {
						String[] code = content.replace("\n","\\n").replaceAll("^.*?```(.+?)```.*?$","$1").replace("\\n","\n").split("\n");
						List<String> object = MacroFormatter.format(new ArrayList<>(Arrays.asList(code)));
						MessageBuilder builder = new MessageBuilder();
						builder.withChannel(channel)
								.appendContent("**Formatted script:**")
								.appendQuote(String.join("\n",object));

						builder.send().subscribe();
						break;
					}
					if (!format(channel, message.getAttachments().iterator().next()))
						channel.createMessage(new MessageCreateSpec().setContent("<:red_cross:398120014974287873> **| Something went wrong.**")).subscribe();
					break;
				case "help":
					sendHelp(channel);
			}
		} else {
			sendHelp(channel);
		}
	}

	@Override
	public void onLoad() {

	}

	private void sendHelp(MessageChannel channel) {
		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel);
		builder.appendContent("**Macro command help page**")
				.appendContent("\n`s!macro format ` will run the command. You need to attach your script as .txt file or in a codeblock.");
		builder.send().subscribe();
	}

	private boolean format(MessageChannel channel, Attachment attachment) {

		if(attachment.getFilename().endsWith("txt")) {
			try {
				List<String> file = HTTP.getAsList(attachment.getUrl());
				List<String> object = MacroFormatter.format(file);
				MessageBuilder builder = new MessageBuilder();
				builder.withChannel(channel)
						.appendContent("**Formatted scriptfile:**");

				builder.withAttachment(attachment.getFilename(),String.join("\n",object));
				builder.send().subscribe();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}

		return true;
	}
}
