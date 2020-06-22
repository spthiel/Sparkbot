package me.bot.commands.user;

import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.macro.api.MacroCache;
import me.macro.api.NullStruct;
import me.macro.api.ResponseStruct;
import me.macro.formatter.FormatObject;
import me.macro.formatter.MacroException;
import me.macro.formatter.MacroFormatter;
import me.main.Entry;
import me.main.Prefixes;
import me.main.utils.HTTP;
import me.main.utils.HastebinUtils;

public class Macro implements ICommand {
	
	private static MacroCache cache = new MacroCache();
	
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
		
		return new String[]{"macro", "macromod"};
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
		
		
		if (args.length >= 1 && args[0].contains("\n")) {
			String[] splitted = args[0].split("\n");
			String[] newarray = new String[args.length + 1];
			newarray[0] = splitted[0];
			newarray[1] = splitted[1];
			System.arraycopy(args, 1, newarray, 2, args.length - 1);
			args = newarray;
		}
		
		if (args.length >= 1) {
			switch (args[0]) {
				case "format":
					if (message.getAttachments().isEmpty() && args.length > 1) {
						String code = content.replaceAll("^(?:.*|\\n)*?```((?:.*|\\n)+?)```(?:.*|\\n)*?$", "$1").trim();
						
						boolean[] flags = getFlags(content);
						
						FormatObject result = new MacroFormatter(code, flags[0], flags[2]).format();
						printObject(channel, result, "temp.txt", flags[1], flags[3]);
						break;
					}
					
					if (message.getAttachments().isEmpty()) {
						sendHelp(channel);
						return;
					}
					
					
					if (!format(channel, content, message.getAttachments().iterator().next())) {
						channel
								.createMessage(spec -> spec.setContent(
										"<:red_cross:398120014974287873> **| Something went wrong.**"))
								.subscribe();
					}
					break;
				case "help":
					sendHelp(channel);
					break;
				case "info":
					execInfo(bot, author, channel, guild, message, command, args, content);
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
			   .appendContent("Formatted scriptfile: ");
		if (object.checksIndepth()) {
			builder.appendContent("\nFound " + object.getExceptions().size() + " errors.");
			if (debug) {
				for (Entry<Integer, MacroException> entry : object.getExceptions()) {
					builder.appendContent("\nLine: " + entry.getKey() + ": " + entry.getValue().toString());
				}
			}
		}
		List<Integer> errors = object.getCriticalErrors();
		if (!errors.isEmpty()) {
			builder.appendContent("\nCritical error" + (errors.size() == 1 ? "" : "s") + " in line:\n");
			for (int i = 0 ; i < errors.size() ; i++) {
				int line = errors.get(i);
				if (i < errors.size() - 2) {
					builder.appendContent(line + ", ");
				}
				if (i == errors.size() - 2) {
					builder.appendContent(line + " and ");
				}
				if (i == errors.size() - 1) {
					builder.appendContent(line + "");
				}
			}
		}
		if (asFile) {
			builder.withAttachment(name, String.join("\n", object.getFormatted()));
		} else {
			Optional<String> url = HastebinUtils.getUrl(HastebinUtils.postCode(object.getFormatted()));
			if (url.isPresent()) {
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
		builder.appendContent("**Macro command help page**")
			   .appendContent("\n`s!macro info [ACTION:/VAR:/IT:/PAR:]<name>`")
			   .appendContent("\nLists the info of the specified action/variable/iterator/parameter")
			   .appendContent(
					   "\nAdd the optional <thing>: to the name to search in the specified subcategory, useful for duplicate entries")
			   .appendContent(
					   "\n`s!macro format <flags>` will run the command mit set flags. You need to attach your script as .txt file.")
			   .appendContent("\n")
			   .appendContent("\n**Possible flags:**")
			   .appendContent("\n`--full` adds syntax checking of the commands")
			   .appendContent("\n`--caps` will set all commands to uppercase instead of lowercase.")
			   .appendContent(
					   "\n`--debug` will display the cause of errors. Might exceed 2000 char limit, use it wisely.")
			   .appendContent("\n`--file` will return the result as attachment instead of as file.");
		channel.createMessage(builder.build()).subscribe();
	}
	
	private boolean format(TextChannel channel, String message, Attachment attachment) {
		
		boolean[] flags = getFlags(message);
		
		if (attachment.getFilename().endsWith("txt")) {
			try {
				String       file   = HTTP.getAsString(attachment.getUrl());
				FormatObject object = new MacroFormatter(file, flags[0], flags[2]).format();
				printObject(channel, object, attachment.getFilename(), flags[1], flags[3]);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
	private static String[] toCheck = {"full", "debug", "caps", "file"};
	
	private boolean[] getFlags(String message) {
		
		boolean[] out = new boolean[toCheck.length];
		
		for (int i = 0 ; i < toCheck.length ; i++) {
			out[i] = message.contains("--" + toCheck[i]);
		}
		return out;
	}
	
	private void execInfo(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, String[] args, final String content) {
		
		if (args.length < 2) {
			sendHelp(channel);
			return;
		}
		
		String type = null;
		String thing;
		if (args[1].contains(":")) {
			String[] splitted = args[1].split(":");
			type = splitted[0];
			thing = splitted[1];
		} else {
			thing = args[1];
		}
		
		final String finalType  = type;
		String       finalType1 = type;
		Consumer<MessageCreateSpec> spec = s -> {
			try {
				cache.getEntry(finalType1, thing)
					 .subscribe(struct -> {
					
						 if (struct instanceof NullStruct) {
							 s.setContent("<:red_cross:398120014974287873> **|** I couldn't find `" + args[1] + "` in the database.");
						 } else {
							 s.setEmbed(buildEmbed(struct));
						 }
					 });
			} catch (Exception e) {
				s.setContent("<:red_cross:398120014974287873> **|** `" + finalType + "` is not a valid type.");
			}
		};
		channel.createMessage(spec).subscribe();
		
	}
	
	private Consumer<EmbedCreateSpec> buildEmbed(ResponseStruct struct) {
		
		System.out.println(struct);
		
		return spec -> {
			
			spec.setTitle(struct.extendedName.replace("<", "\\<"));
			
			String url = "Direct Url: [$NAME$]($URL$)";
			url = url
					.replace("$NAME$", struct.name)
					.replace("$URL$", "http://beta.mkb.gorlem.ml/" + struct.resource.replace("/api/", ""));
			String category = "Category: [$CATEGORY$]($CATURL$)";
			
			StringBuilder desc = new StringBuilder(url);
			
			if (struct.category != null && struct.type != null) {
				category = category
						.replace("$CATEGORY$", struct.category)
						.replace(
								"$CATURL$",
								"http://beta.mkb.gorlem.ml/docs/" + struct.type.getUrl() + "#" + struct.category.replace(
										" ",
										"%20"
																														)
								);
				desc.append(" | ");
				desc.append(category);
			}
			
			spec.setDescription(desc.toString());
			spec.setColor(Color.of(0xEF6578));
			if (struct.version != null) {
				spec.setFooter(
						"Since Macromod " + struct.version.toString() + " | Minecraft " + struct.version.minecraft,
						"https://spthiel.github.io/files/704612_gear_512x512.png"
							  );
			}
			spec.setAuthor(
					"Macromod",
					null,
					"https://cdn.discordapp.com/icons/300295653907628032/0450d7603dff67d64640780b97c2bbc7.webp"
						  );
			
			if (struct.description != null) {
				if (struct.description.length() > 1000) {
					struct.description = struct.description.substring(0, 1000);
				}
				spec.addField("Description", struct.description, false);
			}
			if (struct.permission != null) {
				if (struct.permission.length() > 1000) {
					struct.permission = struct.permission.substring(0, 1000);
				}
				spec.addField("Permission", struct.permission, true);
			}
			if (struct.example != null) {
				if (struct.example.length() > 950) {
					struct.example = struct.example.substring(0, 950);
				}
				spec.addField("Example", "```" + struct.example.replace("\r", "") + "```", false);
			}
			if (struct.related != null && struct.related.size() > 0) {
				StringBuilder related = new StringBuilder();
				for (ResponseStruct.Related rel : struct.related) {
					String toAppend = "[" + rel.name + "](http://beta.mkb.gorlem.ml/" + rel.resource.replace(
							"/api/",
							""
																											) + ")\n";
					if (related.length() + toAppend.length() < 1000) {
						related.append(toAppend);
					} else {
						related.append("...");
						break;
					}
				}
				spec.addField("Related", related.toString(), true);
			}
			if (struct.links != null && struct.links.size() > 0) {
				StringBuilder links = new StringBuilder();
				for (ResponseStruct.Link link : struct.links) {
					String toAppend = "[" + link.title + "](" + link.url + ")\n";
					if (links.length() + toAppend.length() < 1000) {
						links.append(toAppend);
					} else {
						links.append("...");
						break;
					}
				}
				spec.addField("Links", links.toString(), true);
			}
			ResponseStruct.SinceVersion last = null;
			if (struct.changelog != null && struct.changelog.size() > 0) {
				StringBuilder changelogBuilder = new StringBuilder();
				for (ResponseStruct.Changelog changelog : struct.changelog) {
					String toAppend = "";
					if (changelog.version != null && !changelog.version.equals(last)) {
						if (changelog.version.url != null) {
							toAppend = String.format(
									"[**Version %s \\(for minecraft %s\\)**](%s)\n",
									changelog.version.name.replace("v", ""),
									changelog.version.minecraft,
									changelog.version.url
													);
						} else {
							toAppend = String.format(
									"**Version %s \\(for minecraft %s\\)**\n",
									changelog.version.name.replace("v", ""),
									changelog.version.minecraft
													);
						}
						last = changelog.version;
					}
					
					if (changelog.type.equalsIgnoreCase("Added")) {
						toAppend += " + ";
					} else if (changelog.type.equalsIgnoreCase("Deprecated")) {
						toAppend += " - ";
					} else {
						toAppend += " ~ ";
					}
					
					if (changelog.message != null) {
						toAppend += changelog.message;
					} else {
						toAppend += changelog.type;
					}
					
					if (changelogBuilder.length() + toAppend.length() < 1000) {
						changelogBuilder.append(toAppend).append("\n");
					} else {
						changelogBuilder.append("...");
						break;
					}
				}
				spec.addField("Changelog", changelogBuilder.toString(), false);
			}
		};
	}
}