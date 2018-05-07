package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.bot.base.configs.HTTP;
import me.macro.FormatObject;
import me.macro.MacroException;
import me.macro.MacroFormatter;
import me.main.Entry;
import me.main.Prefixes;

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
		if(args.length > 1) {
			switch(args[1]) {
				case "format":
					if(message.getAttachments().isEmpty()) {
						channel.createMessage(new MessageCreateSpec().setContent("<:red_cross:398120014974287873> **| You have to attach a txt file.**")).block();
						break;
					}
					if(!format(channel,content,message.getAttachments().iterator().next()))
						channel.createMessage(new MessageCreateSpec().setContent("<:red_cross:398120014974287873> **| Something went wrong.**")).block();
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
		MessageBuilder builder = new MessageBuilder(channel.getClient());
		builder.withChannel(channel);
		builder.appendContent("**Macro command help page**")
				.appendContent("\n`s!macro format <flags>` will run the command mit set flags. You need to attach your script as .txt file.")
				.appendContent("\n")
				.appendContent("\n**Possible flags:**")
				.appendContent("\n`--full` adds syntax checking of the commands")
				.appendContent("\n`--caps` will set all commands to uppercase instead of lowercase.")
				.appendContent("\n`--diff` will include the amount of changed lines")
				.appendContent("\n`--debug` will display the cause of errors. Might exceed 2000 char limit, use it wisely.");
		builder.send();
	}

	private boolean format(MessageChannel channel, String message, Attachment attachment) {


		boolean fullcheck = false;
		boolean linenumbers = false;
		boolean debug = false;
		boolean caps = false;

		if(message.contains("--full"))
			fullcheck = true;
		if(message.contains("--diff"))
			linenumbers = true;
		if(message.contains("--debug"))
			debug = true;
		if(message.contains("--caps"))
			caps = true;

		if(attachment.getFilename().endsWith("txt")) {
			try {
				List<String> file = HTTP.getAsList(attachment.getUrl());
				FormatObject object = MacroFormatter.format(file,fullcheck,linenumbers,caps);
				MessageBuilder builder = new MessageBuilder(channel.getClient());
				builder.withChannel(channel)
						.appendContent("Formatted scriptfile:");
				if(fullcheck) {
					builder.appendContent("\nFound " + object.getExceptions().size() + " errors.");
					if(debug)
						for(Entry<Integer,MacroException> entry : object.getExceptions()) {
							builder.appendContent("\nLine: " + entry.key + ": " + entry.value.toString());
						}
				}
				if(linenumbers)
					builder.appendContent("\nChanged " + object.getChangedLines().size() + " lines.");
				List<Integer> errors = object.getCriticalErrors();
				if(!errors.isEmpty()) {
					builder.appendContent("\nCritical error"+ (errors.size() == 1 ? "" : "s") + " in line:\n");
					for (int i = 0; i < errors.size(); i++) {
						int line = errors.get(i);
						if(i < errors.size()-2)
							builder.appendContent(line + ", ");
						if(i == errors.size()-2)
							builder.appendContent(line + " and ");
						if(i == errors.size()-1)
							builder.appendContent(line + "");
					}
				}
				builder.withAttachment(attachment.getFilename(),String.join("\n",object.getFormatted()));
				builder.send();
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
