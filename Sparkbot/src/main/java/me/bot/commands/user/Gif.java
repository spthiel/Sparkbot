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
import me.bot.base.MessageBuilder;
import me.main.utils.DiscordUtils;
import me.bot.gifs.Gifmanager;
import me.main.Messages;
import me.main.Prefixes;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class Gif implements ICommand {

	private static Gif instance;
	private List<Gifmanager> gifmanager;
	private String commands = "gif";
	private HashMap<Snowflake,String> lastGifs;
	private TextChannel reportChannel;

	public Gif() {
		instance = this;
	}
	
	public TextChannel getReportChannel() {
		return reportChannel;
	}

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
				if(args[0].equalsIgnoreCase("report")) {
					if(!lastGifs.containsKey(author.getId())) {
						channel.createMessage(spec -> spec.setContent(Messages.Emoji.RED_CROSS.toString() + " | **You haven't used the gif command yet**")).subscribe();
						return;
					}

					String image = lastGifs.get(author.getId());
					String m = "[IMAGE REPORT] From " + author.getUsername() + "#" + author.getDiscriminator() + " " + image;

					Consumer<EmbedCreateSpec> reportSpec = embed -> embed
							.setColor(new Color(0xE84112))
							.setTitle("**`IMAGE REPORT`**")
							.setThumbnail(image)
							.addField("Reporter",author.getUsername() + "#" + author.getDiscriminator(),true)
							.addField("Guild",guild.getName(),true)
							.addField("Guild ID",guild.getId().asString(), true)
							.addField("Additional args:", Arrays.toString(args).replace("report,",""),false)
							.addField("Image",image,false)
							;
					String successfull = "Successfully reported <" + image + ">";
					if(reportChannel == null) {
						System.out.println(m);
						channel.createMessage(spec -> spec.setContent(successfull)).subscribe();
					} else {
						reportChannel.createMessage(spec -> spec.setEmbed(reportSpec)).subscribe(
								ignored -> channel.createMessage(spec -> spec.setContent(successfull)).subscribe(),
								ignored -> {
									System.out.println(m);
									channel.createMessage(spec -> spec.setContent(successfull)).subscribe();
								}
						);
					}
				} else
					parseCommand(bot, guild, channel, author, args[0], (args.length > 1 ? args[1] : null));
			} else {
				sendHelp(channel);
			}
		} else {
			parseCommand(bot, guild, channel, author, commandname, (args.length > 0 ? args[0] : null));
		}
	}

	private void parseCommand(Bot bot, Guild guild, TextChannel channel, Member author, String command, String arg) {
		String authorname = author.getDisplayName();
		if(arg == null) {
			Gifmanager manager = getGifmanager(command);
			sendImage(manager,channel,author,authorname,"me");
			return;
		}

		if (arg.matches("<@!?(\\d{8,})>")) {
			String id = arg.replaceAll("<@!?(\\d+)>", "$1");
			if(id.equalsIgnoreCase(bot.getBotuser().getId().asString())) {
				Gifmanager manager = getGifmanager(command);
				sendImage(manager,channel,author,authorname,"me");
			} else {
				DiscordUtils.getMember(bot, guild, id).subscribe(
						member -> {
							Gifmanager manager = getGifmanager(command);
							sendImage(manager,channel,author,authorname,member.getDisplayName());
						}
				);
			}

		} else if (arg.matches("\\d{8,}")) {
			if(arg.equalsIgnoreCase(bot.getBotuser().getId().asString())) {
				Gifmanager manager = getGifmanager(command);
				if (manager != null)
					manager.run(channel, authorname, "me");
			} else {
				DiscordUtils.getMember(bot, guild, arg).subscribe(
						member -> {
							Gifmanager manager = getGifmanager(command);
							sendImage(manager,channel,author,authorname,member.getDisplayName());
						}
				);
			}
		} else {
			DiscordUtils.getMember(bot, guild, arg).subscribe(
					member -> {
						Gifmanager manager = getGifmanager(command);
						if (manager != null) {
							String name;
							if(member.getId().equals(bot.getBotuser().getId()))
								name = "me";
							else
								name = member.getDisplayName();

							sendImage(manager,channel,author,authorname,name);
						}
					}
			);
		}

	}

	private void sendImage(Gifmanager manager,TextChannel channel, Member member, String executor, String username) {
		if(manager != null) {
			String img = manager.run(channel, executor, username);
			lastGifs.put(member.getId(), img);
		}
	}

	private void sendHelp(TextChannel channel) {
		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel);
		builder.appendContent("Ugly help page for gif: \n");
		builder.appendContent("`s!gif <option> <user>` or `s!<option> <user>`\n");
		builder.appendContent("Use `s!gif report <reason>` if your previous request didn't display an image\n");
		builder.appendContent("Options are:\n");
		gifmanager.forEach(manager -> builder.appendContent("`" + manager.getName() + " (" + manager.length() + " image" + (manager.length() > 1 ? "s" : "") + ")` "));
		builder.send().subscribe();
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

		HashMap<String,Object> config = bot.getResourceManager().getConfig("configs/main","gifs.json");
		if(config.keySet().isEmpty())
			bot.getResourceManager().writeConfig("configs/main","gifs.json",config);

		loadGiffmanager(bot);

		bot.getClient().getChannelById(Snowflake.of(459658855144488960L))
				.filter(channel -> channel instanceof TextChannel)
				.map(channel -> (TextChannel)channel)
				.subscribe(
					channel -> {
						reportChannel = channel;
						System.out.println("Successfully set report channel");
					},
					error -> System.err.println("Something went wrong whilst trying to get the image report channel."),
					() -> System.out.println("End of getting report channel")
		);
		lastGifs = new HashMap<>();
	}

	public void loadGiffmanager(Bot bot) {

		gifmanager = new ArrayList<>();
		commands = "gif";

		Map<String,Object> config = bot.getResourceManager().getConfig("configs/main","gifs.json");

		for(String key : config.keySet()) {
			try {
				
				//noinspection unchecked
				Map<String,Object> entry = (Map<String, Object>) config.get(key);

				String replacer = (String)entry.get("repl");
				
				//noinspection unchecked
				ArrayList<String> gifs = (ArrayList<String>) entry.get("gifs");

				addGifmanager(new Gifmanager(key, gifs.toArray(new String[gifs.size()]), replacer));
			} catch(Exception ignored) {

			}
		}
	}

	private void addGifmanager(Gifmanager manager) {
		gifmanager.add(manager);
		commands += "|" + manager.getName();
	}

	public static Gif getInstance() {
		return instance;
	}
}
