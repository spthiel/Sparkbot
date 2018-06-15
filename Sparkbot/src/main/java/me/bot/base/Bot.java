package me.bot.base;

import discord4j.core.ClientBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.User;
import me.bot.base.configs.PermissionManager;
import me.bot.base.configs.ResourceManager;
import me.bot.base.polls.Poll;
import me.bot.base.utils.DiscordUtils;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Bot {

	private static List<Bot> bots = new ArrayList<>();

    private DiscordClient client;
    private User botuser;
    private Listener listener;
    private String name;
    private String url;
    private boolean streaming;
    private ResourceManager resourceManager;
    private PermissionManager permissionManager;
    private DiscordUtils utils;
    private long startTime;

    public Bot(String token, String name, String basefolder, String commandPackage) {

        this.streaming = false;

	    createBot(token, name, basefolder, commandPackage);

    }

    public Bot(String token, String name, String basefolder, String streamingurl, String commandPackage) {

        this.streaming = true;
        this.url = streamingurl;

        createBot(token, name, basefolder, commandPackage);

    }

    private void createBot(String token, String name, String basefolder, String commandPackage) {

    	bots.add(this);

	    this.name = name;

	    this.resourceManager = new ResourceManager(basefolder);
	    this.permissionManager = new PermissionManager(this);
	    this.utils = new DiscordUtils(this);

	    client = createClient(token);
	    initListeners(commandPackage);
	    client.getApplicationInfo().subscribe(
	    		value -> client.getUserById(value.getId()).subscribe(
					    v -> {
						    botuser = v;
						    System.out.println("Successfully got the botuser: " + botuser.getUsername());
					    },
					    Throwable::printStackTrace
			    ),
			    Throwable::printStackTrace,
	        () -> System.out.println("Successfully got bot id")
	    );
    }

    private void initListeners(String commandPackage) {

	    EventDispatcher dispatcher = client.getEventDispatcher();
	    listener = new Listener(this);
	    dispatcher.on(MessageDeleteEvent.class).subscribe(listener::onDelete);
//	    dispatcher.on(ReadyEvent.class).subscribe(listener::onReadyEvent);
	    dispatcher.on(GuildCreateEvent.class).subscribe(listener::onJoinServer);
	    dispatcher.on(MessageCreateEvent.class).subscribe(listener::onMessageReceivedEvent);

	    Reflections reflections = new Reflections(commandPackage);
	    reflections.getSubTypesOf(ICommand.class).forEach(i -> {
		    try {
			    ICommand command = i.newInstance();
			    addCommands(command);
		    } catch (Exception ignored) {
		    }

	    });

    }

	public long getStartTime() {
		return startTime;
	}

	public User getBotuser() {
    	return botuser;
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

    public void addCommands(ICommand... command){
        listener.addCommands(this, command);
    }

    public Poll addPoll(Poll poll){
        listener.addPoll(poll);
        return poll;
    }

    public List<ICommand> getCommands(){
        return listener.getCommands();
    }

    public String getName(){
        return name;
    }
    
    public DiscordClient getClient() {
        return client;
    }
    
    public void disable() {
        System.out.println("Disabling");
        if (client != null)
            client.logout();
    }

    public void login() {
    	startTime = System.currentTimeMillis();
    	client.login().block();
    }
    
    private DiscordClient createClient(String token) {
        ClientBuilder clientBuilder = new ClientBuilder(token);
        return clientBuilder.build();
    }

	public PermissionManager getPermissionManager() {
		return permissionManager;
	}


	public static Bot getBotByName(String name) {
    	if(bots.size() == 0)
    		return null;
    	if(name.trim().equalsIgnoreCase(""))
    		return bots.get(0);
		return bots.stream().filter(bot -> bot.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
