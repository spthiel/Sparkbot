package me.bot.commands.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.soap.Text;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageBuilder;
import me.main.utils.DiscordUtils;
import me.bot.gifs.Gifmanager;
import me.main.Messages;
import me.main.Prefixes;
import me.main.utils.HTTP;

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
				} else {
					parseCommand(bot, guild, channel, author, args[0], 1, (args.length > 1 ? args : null));
				}
			} else {
				sendHelp(channel);
			}
		} else {
			parseCommand(bot, guild, channel, author, commandname, 0, (args.length > 0 ? args : null));
		}
	}

	private void parseCommand(Bot bot, Guild guild, TextChannel channel, Member author, String command, int beginindex, String[] args) {
		String authorname = author.getDisplayName();
		Gifmanager manager = getGifmanager(command);
		if(args == null) {
			sendImage(manager,channel,author,authorname,"me");
			return;
		}
		
		Flux.fromArray(args).flatMap(arg -> map(bot, guild, arg))
			.distinct()
			.collectList()
			.subscribe(members -> prepare(bot, manager, channel, author, authorname, members));
	}
	
	private Flux<Member> map(Bot bot, Guild guild, String arg) {
	    String lcase = arg.toLowerCase();
		if (arg.matches("<@&(\\d{8,})>")) {
			String id = arg.replaceAll("<@&(\\d{8,})>", "$1");
			return guild.getRoleById(Snowflake.of(id)).flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
		} else if (lcase.startsWith("role:")) {
			if(lcase.matches("role:\\d+")) {
				String id = lcase.replaceAll("role:(\\d+)","$1");
				return guild.getRoleById(Snowflake.of(id)).flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
			} else {
				String rolename = arg.replaceAll("[rR]ole:([^ ]+)","$1");
				return DiscordUtils.getRoleByName(bot, guild, rolename)
							 .flatMapMany(role -> DiscordUtils.getRoleMembers(guild, role, 50));
			}
		} else if (arg.matches("<@!?(\\d{8,})>")) {
			String id = arg.replaceAll("<@!?(\\d+)>", "$1");
			return Flux.from(DiscordUtils.getMember(bot, guild, id));
		} else {
			return Flux.from(DiscordUtils.getMember(bot, guild, arg));
		}
	}
	
	private void prepare(Bot bot, Gifmanager manager, TextChannel channel, Member author, String authorname, List<Member> members) {
		if (manager != null) {
			sendImage(manager,channel,author,authorname,formatMembers(bot, author, members));
		}
	}
	
	private String formatMembers(Bot bot, Member author, List<Member> members) {
		LinkedList<String> names = new LinkedList<>();
		boolean containsMe = false;
		boolean containsThem = false;
		for (int i = 0 ; i < members.size() ; i++) {
			Member member = members.get(i);
			if(member.getId().equals(bot.getBotuser().getId()))
				containsMe = true;
			else if (member.getId().equals(author.getId()))
				containsThem = true;
			else
				names.add(member.getDisplayName());
		}
		StringBuilder out = new StringBuilder();
		int maxlength = 100;
        names.sort(String.CASE_INSENSITIVE_ORDER);
		if(containsMe) {
            names.addFirst("me");
		}
		if(containsThem) {
            names.addFirst("themself");
		}
		Iterator<String> it = names.iterator();
		for (int i = 0 ; i < names.size() ; i++) {
			if(i == names.size() - 1 && (names.size() != 1)) {
				out.append(" and ");
			} else if(i > 0) {
				out.append(", ");
				maxlength -= 2;
			}
			String name = it.next();
			maxlength -= name.length();
			out.append(name);
			if(maxlength <= 0 && names.size()-1 != i) {
				out.append(" and ").append((names.size()-1) - i).append(" more");
				break;
			}
		}
		return out.toString();
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
		builder.appendContent("Not as ugly help page for gif: \n");
		builder.appendContent("`s!gif <option> <user>` or `s!<option> <user>`\n");
		builder.appendContent("Use `s!gif report <reason>` if your previous request didn't display an image\n");
		builder.appendContent("Options are:\n");
		getLongest();
		gifmanager.forEach(manager -> builder.appendContent("`" + expand(manager) +"` \n"));
		builder.send().subscribe();
	}
	
	private int longest = -1;
	private int longestnum = -1;

	private void getLongest() {
		if(longest != -1) {
			return;
		}
		longest = gifmanager.stream().mapToInt(manager -> manager.getName().length()).max().orElse(-1);
		longestnum = gifmanager.stream().mapToInt(manager -> (manager.length() + "").length()).max().orElse(-1);
	}
	
	private String extra;
	private String extranum;
	
	private String expand(Gifmanager manager) {
		if(extra == null) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i <= longest; i++) {
				builder.append(" ");
			}
			extra = builder.toString();
		}
		if(extranum == null) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < longestnum; i++) {
				builder.append(" ");
			}
			extranum = builder.toString();
		}
		String name = manager.getName();
		int number = manager.length();
		String format = "%s%s(%s%d image%s)";
		return String.format(format, name, extra.substring(name.length()), extranum.substring((number + "").length()), number, number > 1 ? "s" : " ");
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
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public Map<String, Object> tryLoadFromGithub() {
		
		try {
			String json = HTTP.getAsString("https://raw.githubusercontent.com/spthiel/Sparkbot/master/Sparkbot/resources/configs/main/gifs.json");
			
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
			
		} catch (Exception e) {
			return null;
		}
		
	}

	public void loadGiffmanager(Bot bot) {

		gifmanager = new ArrayList<>();
		commands = "gif";

		
		Map<String,Object> config;
		config = tryLoadFromGithub();
		if(config == null) {
			config = bot.getResourceManager().getConfig("configs/main", "gifs.json");
		}
		
		ArrayList<String> sorted = new ArrayList<>(config.keySet());
		sorted.sort(String.CASE_INSENSITIVE_ORDER);

		for(String key : sorted) {
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
