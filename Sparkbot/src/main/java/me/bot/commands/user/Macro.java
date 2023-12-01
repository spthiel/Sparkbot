package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;

import java.util.Arrays;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.macro.api.MacroCache;
import me.macro.api.NullStruct;
import me.macro.api.ResponseStruct;
import me.main.Prefixes;

@SuppressWarnings("unused")
public class Macro implements ICommand {
	
	private static final MacroCache cache = new MacroCache();
	
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
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, String[] args, final String content) {
		
		
		if (args.length >= 1 && args[0].contains("\n")) {
			String[] splitted = args[0].split("\n");
			String[] newArray = new String[args.length + 1];
			newArray[0] = splitted[0];
			newArray[1] = splitted[1];
			System.arraycopy(args, 1, newArray, 2, args.length - 1);
			args = newArray;
		}
		
		if (args.length >= 1) {
			switch (args[0]) {
				case "tut":
				case "tutorials":
					sendTutorials(channel);
					break;
				case "help":
					sendHelp(channel);
					break;
				case "info":
					execInfo(channel, args);
					break;
				default:
					execInfo(channel, add2BeginningOfArray(args, "info"));
			}
		} else {
			sendHelp(channel);
		}
	}
	
	public static <T> T[] add2BeginningOfArray(T[] elements, T element) {
		
		T[] newArray = Arrays.copyOf(elements, elements.length + 1);
		newArray[0] = element;
		System.arraycopy(elements, 0, newArray, 1, elements.length);
		
		return newArray;
	}
	
	@Override
	public void onLoad(final Bot bot) {
	
	}
	
	private void sendTutorials(MessageChannel channel) {
		
		EmbedCreateSpec embed = EmbedCreateSpec
			.builder()
			.color(Color.of(0xEF6578))
			.addField("Mumfrey: Quickstart into scripting (single video)", "[Minecraft Macro/Keybind Mod - Scripting Tutorial Part 1](https://www.youtube.com/watch?v=YkBAXQu2GEM)", false)
			.addField("Lezappen: More in depth tutorials on macromod (series)", "[Macro Mod Tutorials](https://www.youtube.com/playlist?list=PLQ1oeCLHyXfiSKqOeyeBu27ZkN-a9gl__)", false)
			.addField("Gorlem: Documentation for in-game actions (website)", "[Docs](https://mkb.ddoerr.com/docs/actions)", false)
			.addField("Elspeth: List of usable modules (website)", "[Modules](https://spthiel.github.io/Modules/)", false)
			.addField("Elspeth: Various information (channel)", "<#366221638750175232>", false)
			.build();
		channel.createMessage(embed).subscribe();
	}
	
	private void sendHelp(MessageChannel channel) {
		
		MessageBuilder builder = new MessageBuilder();
		builder.appendContent("**Macro command help page**")
			   .appendContent("\n`s!macro [info] [ACTION:/VAR:/IT:/PAR:]<name>`")
			   .appendContent("\nLists the info of the specified action/variable/iterator/parameter")
			   .appendContent(
				   "\nAdd the optional <thing>: to the name to search in the specified subcategory, useful for duplicate entries");
		channel.createMessage(builder.build())
			   .subscribe();
	}
	
	private void execInfo(final TextChannel channel, String[] args) {
		
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
		
		final String              finalType  = type;
		String                    finalType1 = type;
		MessageCreateSpec.Builder builder    = MessageCreateSpec.builder();
		try {
			cache.getEntry(finalType1, thing)
				 .subscribe(struct -> {
				
					 if (struct instanceof NullStruct) {
						 builder.content("<:red_cross:398120014974287873> **|** I couldn't find `" + args[1].replace('`', ' ') + "` in the database.");
					 } else {
						 builder.addEmbed(buildEmbed(struct));
					 }
				 });
		} catch (Exception e) {
			builder.content("<:red_cross:398120014974287873> **|** `" + finalType.replace('`', ' ') + "` is not a valid type.");
		}
		
		channel.createMessage(builder.build())
			   .subscribe();
		
	}
	
	@SuppressWarnings("ExtractMethodRecommender")
	private EmbedCreateSpec buildEmbed(ResponseStruct struct) {
		
		EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
		
		builder.title(struct.extendedName.replace("<", "\\<"));
		
		String url = "Direct Url: [$NAME$]($URL$)";
		url = url
			.replace("$NAME$", struct.name)
			.replace("$URL$", "https://mkb.ddoerr.com/" + struct.resource.replace("/api/", ""));
		String category = "Category: [$CATEGORY$]($CATURL$)";
		
		StringBuilder desc = new StringBuilder(url);
		
		if (struct.category != null && struct.type != null) {
			category = category
				.replace("$CATEGORY$", struct.category)
				.replace(
					"$CATURL$",
					"https://mkb.ddoerr.com/docs/" + struct.type.getUrl() + "#" + struct.category.replace(
						" ",
						"%20"
					)
				);
			desc.append(" | ");
			desc.append(category);
		}
		
		builder.description(desc.toString());
		builder.color(Color.of(0xEF6578));
		if (struct.version != null) {
			builder.footer(
				"Since Macromod " + struct.version + " | Minecraft " + struct.version.minecraft,
				"https://spthiel.github.io/files/704612_gear_512x512.png"
			);
		}
		builder.author(
			"Macromod",
			null,
			"https://cdn.discordapp.com/icons/300295653907628032/0450d7603dff67d64640780b97c2bbc7.webp"
		);
		
		if (struct.description != null) {
			if (struct.description.length() > 1000) {
				struct.description = struct.description.substring(0, 1000);
			}
			builder.addField("Description", struct.description, false);
		}
		if (struct.permission != null) {
			if (struct.permission.length() > 1000) {
				struct.permission = struct.permission.substring(0, 1000);
			}
			builder.addField("Permission", struct.permission, true);
		}
		if (struct.example != null) {
			if (struct.example.length() > 950) {
				struct.example = struct.example.substring(0, 950);
			}
			builder.addField("Example", "```" + struct.example.replace("\r", "") + "```", false);
		}
		if (struct.related != null && !struct.related.isEmpty()) {
			StringBuilder related = new StringBuilder();
			for (ResponseStruct.Related rel : struct.related) {
				String toAppend = "[" + rel.name + "](https://mkb.ddoerr.com/" + rel.resource.replace(
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
			builder.addField("Related", related.toString(), true);
		}
		if (struct.links != null && !struct.links.isEmpty()) {
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
			builder.addField("Links", links.toString(), true);
		}
		ResponseStruct.SinceVersion last = null;
		if (struct.changelog != null && !struct.changelog.isEmpty()) {
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
					changelogBuilder.append(toAppend)
									.append("\n");
				} else {
					changelogBuilder.append("...");
					break;
				}
			}
			builder.addField("Changelog", changelogBuilder.toString(), false);
		}
		return builder.build();
	}
}