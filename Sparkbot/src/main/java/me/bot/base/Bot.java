package me.bot.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.DisconnectEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Color;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.reflections.Reflections;

import me.bot.base.configs.Language;
import me.bot.base.configs.PermissionManager;
import me.bot.base.configs.ResourceManager;
import me.bot.base.polls.Poll;
import me.bot.commands.user.Gif;
import me.main.utils.BotsOnDiscordUtils;
import me.main.utils.DiscordUtils;
import me.main.utils.ExceptionUtils;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Bot {
	
	private static List<Bot> bots = new ArrayList<>();
	
	public static void foreach(Consumer<? super Bot> consumer) {
		
		bots.forEach(consumer);
	}
	
	private DiscordClient client;
	private GatewayDiscordClient gateway;
	private Listener      listener;
	private String        name;
	private String        url;
	
	private HashMap<String, String> apiKeys;
	
	private boolean           streaming;
	private ResourceManager   resourceManager;
	private PermissionManager permissionManager;
	private DiscordUtils      utils;
	private long              startTime;
	
	public Bot(String basefolder, String configfile) {
		
		File         file   = new File(basefolder, configfile);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		if (!file.exists()) {
			File folder = file.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			try {
				file.createNewFile();
				BotConstruct exampleConstruct = new BotConstruct();
				String       json             = mapper.writeValueAsString(exampleConstruct);
				ResourceManager.writeFile(json, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			throw new NullPointerException("Bot config file does not exist: " + file.getPath() + " | Create example file.");
		}
		
		String json = ResourceManager.readFileAsString(file);
		
		try {
			BotConstruct construct = mapper.readValue(json, new TypeReference<BotConstruct>() {
			});
			
			String                    languagePath  = construct.LANGUAGEPATH;
			Class<? extends Language> languageClass = null;
			if (!languagePath.equalsIgnoreCase("null")) {
				try {
					Class<?> clazz = Class.forName(languagePath);
					if (clazz.isAssignableFrom(Language.class)) {
						languageClass = (Class<? extends Language>) clazz;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			if (construct.TWITCHURL.equalsIgnoreCase("OPTIONAL") || construct.TWITCHURL.equalsIgnoreCase("null")) {
				this.streaming = false;
			} else {
				this.streaming = true;
				this.url = construct.TWITCHURL;
			}
			
			createBot(
					construct.TOKEN,
					construct.NAME,
					basefolder,
					construct.COMMAND_PACKAGE,
					languageClass,
					construct.apikeys
					 );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Deprecated
	public Bot(String token, String name, String basefolder, String commandPackage, Class<? extends Language> language, HashMap<String, String> keys) {
		
		this.streaming = false;
		
		createBot(token, name, basefolder, commandPackage, language, keys);
		
	}
	
	@Deprecated
	public Bot(String token, String name, String basefolder, String streamingurl, String commandPackage, Class<? extends Language> language, HashMap<String, String> keys) {
		
		this.streaming = true;
		this.url = streamingurl;
		
		createBot(token, name, basefolder, commandPackage, language, keys);
		
	}
	
	private void createBot(String token, String name, String basefolder, String commandPackage, Class<? extends Language> language, HashMap<String, String> apikeys) {
		
		bots.add(this);
		
		this.name = name;
		this.apiKeys = apikeys;
		
		this.resourceManager = new ResourceManager(basefolder, language);
		this.permissionManager = new PermissionManager(this);
		this.utils = new DiscordUtils(this);
		
		client = createClient(token);
		initListeners(commandPackage);
		
		Optional<String> bondkey = getApiKey("botsondiscord");
		bondkey.ifPresent(this :: startBotsOnDiscordInterval);
	}
	
	private void startBotsOnDiscordInterval(final String key) {
		
		Flux.interval(Duration.ofMinutes(2), Duration.ofHours(24)).subscribe(
				ignored -> BotsOnDiscordUtils.updateGuildCount(this, key),
				Throwable:: printStackTrace
																			);
	}
	
	private void initListeners(String commandPackage) {
		
		gateway = client.login().block();
		listener = new Listener(this);
		gateway.on(MessageDeleteEvent.class).subscribe(listener:: onDelete);
		gateway.on(ReadyEvent.class).subscribe(listener:: onReadyEvent);
		gateway.on(GuildCreateEvent.class).subscribe(listener:: onJoinServer);
		setupMessageCreateListener();
		gateway.on(DisconnectEvent.class).subscribe(event -> {
			System.out.println(name + " disconnected");
		});
		gateway.on(ReadyEvent.class).subscribe(event -> System.out.println(name + " logged in"));
		
		Reflections reflections = new Reflections(commandPackage);
		reflections.getSubTypesOf(ICommand.class).forEach(i -> {
			try {
				ICommand command = i.newInstance();
				if(command instanceof IDisabledCommand) {
				    return;
                }
				addCommands(command);
			} catch (Exception ignored) {
			}
			
		});
		
	}
	
	private Disposable  messageCreateSubscriber = null;
	private TextChannel reportChannel;
	
	public void setupMessageCreateListener() {
		
		
		if (messageCreateSubscriber != null && !messageCreateSubscriber.isDisposed()) {
			messageCreateSubscriber.dispose();
			System.out.println("Disposed listener");
		}
		messageCreateSubscriber = gateway.on(MessageCreateEvent.class)
			.subscribe(
				event -> {
					// WARNING: Dirty code, do not use this
					try {
						listener.onMessageReceivedEvent(event);
					} catch (Exception e) {
						String stackTrace = ExceptionUtils.makePrintAble(e);
						Gif.getInstance().getReportChannel()
						   .createMessage(mspec -> {
								mspec.setEmbed(
									spec -> {
										spec.setTitle("Exception in MessageCreateListener");
										spec.setDescription("```\n" + stackTrace + "\n```");
										spec.setColor(Color.of(0xE84112));
									});
								}
							).subscribe();
						e.printStackTrace();
					}
					// END
				}
			);
		System.out.println("Added listener");
		
	}
	
	public long getStartTime() {
		
		return startTime;
	}
	
	public boolean isStreaming() {
		
		return streaming;
	}
	
	public ResourceManager getResourceManager() {
		
		return resourceManager;
	}
	
	public DiscordUtils getUtils() {
		
		return utils;
	}
	
	public String getUrl() {
		
		return url;
	}
	
	public void addCommands(ICommand... command) {
		
		listener.addCommands(this, command);
	}
	
	public Poll addPoll(Poll poll) {
		
		listener.addPoll(poll);
		return poll;
	}
	
	public List<ICommand> getCommands() {
		
		return listener.getCommands();
	}
	
	public String getName() {
		
		return name;
	}
	
	public Optional<String> getApiKey(String api) {
		
		if (apiKeys.containsKey(api)) {
			return Optional.of(apiKeys.get(api));
		} else {
			return Optional.empty();
		}
	}
	
	public DiscordClient getClient() {
		
		return client;
	}
	
	public void disable() {
		
		System.out.println("Disabling");
		if (client != null) {
			gateway.logout().subscribe();
		}
	}
	
	public void login() {
		
		System.out.println("Logging in");
		startTime = System.currentTimeMillis();
		client.login().subscribe(
				aVoid -> {
				},
				Throwable:: printStackTrace
								);
	}
	
	private DiscordClient createClient(String token) {
		
		return DiscordClient.create(token);
	}
	
	public PermissionManager getPermissionManager() {
		
		return permissionManager;
	}
	
	public GatewayDiscordClient getGateway() {
		
		return gateway;
	}
	
	public static Bot getBotByName(String name) {
		
		if (bots.size() == 0) {
			return null;
		}
		if (name.trim().equalsIgnoreCase("")) {
			return bots.get(0);
		}
		return bots.stream().filter(bot -> bot.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
