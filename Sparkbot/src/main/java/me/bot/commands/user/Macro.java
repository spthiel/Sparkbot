package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.main.utils.HTTP;
import me.macro.FormatObject;
import me.macro.MacroException;
import me.macro.MacroFormatter;
import me.main.Entry;
import me.main.utils.HastebinUtils;
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
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, String[] args, final String content) {


		if(args.length >= 1 && args[0].contains("\n")) {
			String[] splitted = args[0].split("\n");
			String[] newarray = new String[args.length+1];
			newarray[0] = splitted[0];
			newarray[1] = splitted[1];
			System.arraycopy(args, 1, newarray, 2, args.length - 1);
			args = newarray;
		}

		if (args.length >= 1) {
			switch (args[0]) {
				case "format":
					if (message.getAttachments().isEmpty() && args.length > 1) {
						String[] code = content.replaceAll("^(?:.|\\n)*?```((?:.|\\n)+?)```(?:.|\\n)*?$","$1").trim().split("\n");

						boolean fullcheck = false;
						boolean linenumbers = false;
						boolean debug = false;
						boolean caps = false;
						boolean file = false;

						if(content.contains("--full"))
							fullcheck = true;
						if(content.contains("--diff"))
							linenumbers = true;
						if(content.contains("--debug"))
							debug = true;
						if(content.contains("--caps"))
							caps = true;
						if(content.contains("--file"))
							file = true;

						FormatObject result = MacroFormatter.format(new ArrayList<>(Arrays.asList(code)),fullcheck,linenumbers,caps);
						printObject(channel, result, "temp.txt",debug,file);
						break;
					}
					
					if(message.getAttachments().isEmpty()) {
						sendHelp(channel);
						return;
					}
					
					
					if (!format(channel, content, message.getAttachments().iterator().next()))
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
	public void onLoad(final Bot bot) {

	}

	private void printObject(MessageChannel channel, FormatObject object, String name, boolean debug, boolean asFile) {

		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel)
				.appendContent("Formatted scriptfile:");
		if(object.checksIndepth()) {
			builder.appendContent("\nFound " + object.getExceptions().size() + " errors.");
			if(debug)
				for(Entry<Integer,MacroException> entry : object.getExceptions()) {
					builder.appendContent("\nLine: " + entry.getKey() + ": " + entry.getValue().toString());
				}
		}
		if(object.inculdesDiff())
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
		if (asFile) {
			builder.withAttachment(name, String.join("\n", object.getFormatted()));
		} else {
			Optional<String> url = HastebinUtils.getUrl(HastebinUtils.postCode(object.getFormatted()));
			if(url.isPresent()) {
				builder.appendContent(url.get());
			} else {
				builder.appendContent("Something went wrong while accessing hastebin, posting as file instead.");
				builder.withAttachment(name, String.join("\n", object.getFormatted()));
			}
			
		}
		builder.send().subscribe();
	}

	private void sendHelp(MessageChannel channel) {
		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel);
		builder.appendContent("**Macro command help page**")
				.appendContent("\n`s!macro format <flags>` will run the command mit set flags. You need to attach your script as .txt file.")
				.appendContent("\n")
				.appendContent("\n**Possible flags:**")
				.appendContent("\n`--full` adds syntax checking of the commands")
				.appendContent("\n`--caps` will set all commands to uppercase instead of lowercase.")
				.appendContent("\n`--diff` will include the amount of changed lines")
				.appendContent("\n`--debug` will display the cause of errors. Might exceed 2000 char limit, use it wisely.")
				.appendContent("\n`--file` will return the result as attachment instead of as file.");
		builder.send().subscribe();
	}

	private boolean format(TextChannel channel, String message, Attachment attachment) {
		
		
		boolean fullcheck = false;
		boolean linenumbers = false;
		boolean debug = false;
		boolean caps = false;
		boolean includeFile = false;
		
		if(message.contains("--full"))
			fullcheck = true;
		if(message.contains("--diff"))
			linenumbers = true;
		if(message.contains("--debug"))
			debug = true;
		if(message.contains("--caps"))
			caps = true;
		if(message.contains("--file"))
			includeFile = true;

		if(attachment.getFilename().endsWith("txt")) {
			try {
				List<String> file = HTTP.getAsList(attachment.getUrl());
				FormatObject object = MacroFormatter.format(file,fullcheck,linenumbers,caps);
				printObject(channel,object,attachment.getFilename(),debug,includeFile);
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