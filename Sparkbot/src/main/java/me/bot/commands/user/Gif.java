package me.bot.commands.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Flux;

import java.util.*;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.bot.gifs.GifManager;
import me.main.Messages;
import me.main.Prefixes;
import me.main.utils.DiscordUtils;
import me.main.utils.HTTP;

@SuppressWarnings("unused")
public class Gif implements ICommand {
	
	private static Gif              instance;
	private        List<GifManager> gitManagers;
	private        String           commands = "gif";
	private        HashMap<Snowflake, String> lastGifs;
	private        TextChannel                reportChannel;
	
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
	public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandName, String[] args, String content) {
		
		if (commandName.equalsIgnoreCase("gif")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("report")) {
					if (!lastGifs.containsKey(author.getId())) {
						channel.createMessage(spec -> spec.setContent(Messages.Emoji.RED_CROSS + " | **You haven't used the gif command yet**"))
							   .subscribe();
						return;
					}
					
					String image = lastGifs.get(author.getId());
					String m     = "[IMAGE REPORT] From " + author.getUsername() + " " + image;
					
					EmbedCreateSpec reportSpec = EmbedCreateSpec.builder()
																.color(Color.of(0xE84112))
																.title("**`IMAGE REPORT`**")
																.thumbnail(image)
																.addField("Reporter", author.getUsername(), true)
																.addField("Guild", guild.getName(), true)
																.addField("Guild ID", guild.getId()
																						   .asString(), true)
																.addField("Additional args:", Arrays.toString(args)
																									.replace("report,", ""), false)
																.addField("Image", image, false)
																.build();
					
					String successful = "Successfully reported <" + image + ">";
					if (reportChannel == null) {
						System.out.println(m);
						channel.createMessage(spec -> spec.setContent(successful))
							   .subscribe();
					} else {
						reportChannel
							.createMessage(MessageCreateSpec.builder()
															.addEmbed(reportSpec)
															.build())
							.subscribe(
								ignored -> channel.createMessage(successful)
												  .subscribe(),
								ignored -> {
									System.out.println(m);
									channel.createMessage(successful)
										   .subscribe();
								}
							);
					}
				} else {
					parseCommand(bot, guild, channel, author, args[0], (args.length > 1 ? args : null));
				}
			} else {
				sendHelp(channel);
			}
		} else {
			parseCommand(bot, guild, channel, author, commandName, (args.length > 0 ? args : null));
		}
	}
	
	private void parseCommand(Bot bot, Guild guild, TextChannel channel, Member author, String command, String[] args) {
		
		String     authorName = author.getDisplayName();
		GifManager manager    = getGifmanager(command);
		if (args == null) {
			sendImage(manager, channel, author, authorName, "me");
			return;
		}
		
		Flux.fromArray(args)
			.flatMap(arg -> map(bot, guild, arg))
			.distinct()
			.collectList()
			.subscribe(members -> prepare(bot, manager, channel, author, authorName, members));
	}
	
	private Flux<Member> map(Bot bot, Guild guild, String arg) {
		
		String lowerCase = arg.toLowerCase();
		if (arg.matches("<@&(\\d{8,})>")) {
			String id = arg.replaceAll("<@&(\\d{8,})>", "$1");
			return guild.getRoleById(Snowflake.of(id))
						.flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
		} else if (lowerCase.startsWith("role:")) {
			if (lowerCase.matches("role:\\d+")) {
				String id = lowerCase.replaceAll("role:(\\d+)", "$1");
				return guild.getRoleById(Snowflake.of(id))
							.flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
			} else {
				String roleName = arg.replaceAll("[rR]ole:([^ ]+)", "$1");
				return DiscordUtils.getRoleByName(bot, guild, roleName)
								   .flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
			}
		} else if (arg.matches("<@!?(\\d{8,})>")) {
			String id = arg.replaceAll("<@!?(\\d+)>", "$1");
			return Flux.from(DiscordUtils.getMember(bot, guild, id));
		} else {
			return Flux.from(DiscordUtils.getMember(bot, guild, arg));
		}
	}
	
	private void prepare(Bot bot, GifManager manager, TextChannel channel, Member author, String authorName, List<Member> members) {
		
		if (manager != null) {
			sendImage(manager, channel, author, authorName, formatMembers(bot, author, members));
		}
	}
	
	private String formatMembers(Bot bot, Member author, List<Member> members) {
		
		LinkedList<String> names        = new LinkedList<>();
		boolean            containsMe   = false;
		boolean            containsThem = false;
		for (Member member : members) {
			if (member.getId()
					  .equals(bot.getGateway()
								 .getSelfId())) {
				containsMe = true;
			} else if (member.getId()
							 .equals(author.getId())) {
				containsThem = true;
			} else {
				names.add(member.getDisplayName());
			}
		}
		StringBuilder out       = new StringBuilder();
		int           maxlength = 100;
		names.sort(String.CASE_INSENSITIVE_ORDER);
		if (containsMe) {
			names.addFirst("me");
		}
		if (containsThem) {
			names.addFirst("themself");
		}
		if (names.isEmpty()) {
			names.addFirst("me");
		}
		Iterator<String> it = names.iterator();
		for (int i = 0 ; i < names.size() ; i++) {
			if (i == names.size() - 1 && (names.size() != 1)) {
				out.append(" and ");
			} else if (i > 0) {
				out.append(", ");
				maxlength -= 2;
			}
			String name = it.next();
			maxlength -= name.length();
			out.append(name);
			if (maxlength <= 0 && names.size() - 1 != i) {
				out.append(" and ")
				   .append((names.size() - 1) - i)
				   .append(" more");
				break;
			}
		}
		return out.toString();
	}
	
	private void sendImage(GifManager manager, TextChannel channel, Member member, String executor, String username) {
		
		if (manager != null) {
			String img = manager.run(channel, executor, username);
			lastGifs.put(member.getId(), img);
		}
	}
	
	private void sendHelp(TextChannel channel) {
		
		MessageBuilder builder = new MessageBuilder();
		builder.withChannel(channel);
		builder.appendContent("Not as ugly help page for gif: \n");
		builder.appendContent("`s!gif <option> <user>` or `s!<option> <user>`\n");
		builder.appendContent("Use `s!gif report <reason>` if your previous request didn't display an image\n");
		builder.appendContent("Options are:\n");
		getLongest();
		gitManagers.forEach(manager -> builder.appendContent("`" + expand(manager) + "` \n"));
		builder.send()
			   .subscribe();
	}
	
	private int longest       = -1;
	private int longestNumber = -1;
	
	private void getLongest() {
		
		if (longest != -1) {
			return;
		}
		longest = gitManagers.stream()
							 .mapToInt(manager -> manager.getName()
														 .length())
							 .max()
							 .orElse(-1);
		longestNumber = gitManagers.stream()
								   .mapToInt(manager -> (manager.length() + "").length())
								   .max()
								   .orElse(-1);
	}
	
	private String extra;
	private String extraNumber;
	
	private String expand(GifManager manager) {
		
		if (extra == null) {
			extra = " ".repeat(Math.max(0, longest + 1));
		}
		if (extraNumber == null) {
			extraNumber = " ".repeat(Math.max(0, longestNumber));
		}
		String name   = manager.getName();
		int    number = manager.length();
		String format = "%s%s(%s%d image%s)";
		return String.format(format, name, extra.substring(name.length()), extraNumber.substring((number + "").length()), number, number > 1 ? "s" : " ");
	}
	
	private GifManager getGifmanager(String name) {
		
		for (GifManager manager : gitManagers) {
			if (manager.getName()
					   .equalsIgnoreCase(name)) {
				return manager;
			}
		}
		return null;
	}
	
	@Override
	public void onLoad(Bot bot) {
		
		HashMap<String, Object> config = bot.getResourceManager()
											.getConfig("configs/main", "gifs.json");
		if (config.keySet()
				  .isEmpty()) {
			bot.getResourceManager()
			   .writeConfig("configs/main", "gifs.json", config);
		}
		
		loadGifManager(bot);
		
		bot.getGateway()
		   .getChannelById(Snowflake.of(459658855144488960L))
		   .filter(channel -> channel instanceof TextChannel)
		   .map(channel -> (TextChannel) channel)
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
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public Map<String, Object> tryLoadFromGithub() {
		
		try {
			String json = HTTP.getAsString("https://raw.githubusercontent.com/spthiel/Sparkbot/master/Sparkbot/resources/configs/main/gifs.json");
			
			return mapper.readValue(json, new TypeReference<>() {
			});
			
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public void loadGifManager(Bot bot) {
		
		gitManagers = new ArrayList<>();
		commands = "gif";
		
		
		Map<String, Object> config;
		config = tryLoadFromGithub();
		if (config == null) {
			config = bot.getResourceManager()
						.getConfig("configs/main", "gifs.json");
		}
		
		ArrayList<String> sorted = new ArrayList<>(config.keySet());
		sorted.sort(String.CASE_INSENSITIVE_ORDER);
		
		for (String key : sorted) {
			try {
				
				//noinspection unchecked
				Map<String, Object> entry = (Map<String, Object>) config.get(key);
				
				String replacer = (String) entry.get("repl");
				
				//noinspection unchecked
				ArrayList<String> gifs = (ArrayList<String>) entry.get("gifs");
				
				addGifManager(new GifManager(key, gifs.toArray(new String[0]), replacer));
			} catch (Exception ignored) {
			
			}
		}
	}
	
	private void addGifManager(GifManager manager) {
		
		gitManagers.add(manager);
		commands += "|" + manager.getName();
	}
	
	public static Gif getInstance() {
		
		return instance;
	}
}
