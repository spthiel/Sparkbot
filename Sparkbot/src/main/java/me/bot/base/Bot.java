package me.bot.base;

import discord4j.common.json.payload.dispatch.MessageCreate;
import discord4j.core.ClientBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.User;
import me.bot.base.configs.ResourceManager;
import me.bot.base.polls.Poll;
import me.main.Main;

import java.time.Duration;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Bot {
    
    private DiscordClient client;
    private User botuser;
    private Listener listener;
    private String name;
    private String url;
    private boolean streaming;
    private ResourceManager resourceManager;
    private DiscordUtils utils;

    public Bot(String token, String name, String basefolder) {

        this.streaming = false;

	    createBot(token, name, basefolder);

    }

    public Bot(String token, String name, String basefolder, String streamingurl) {

        this.streaming = true;
        this.url = streamingurl;

        createBot(token, name, basefolder);

    }

    private void createBot(String token, String name, String basefolder) {

	    this.name = name;

	    this.resourceManager = new ResourceManager(basefolder);
	    this.utils = new DiscordUtils(this);

	    client = createClient(token);
	    initListeners();
	    client.getApplicationInfo().subscribe(
	    		value -> client.getUserById(value.getId()).subscribe(
					    v -> {
						    botuser = v;
						    System.out.println("Successfully got the botuser: " + botuser.getUsername());
					    },
					    Throwable::printStackTrace,
					    () -> System.out.println("Something went wrong whilst getting botuser")
			    ),
			    Throwable::printStackTrace,
	        () -> System.out.println("Something went wrong whilst getting bot id")
	    );
	    try {
		    client.login().block(Duration.ofSeconds(3));
	    } catch(IllegalStateException ignore){

	    }
    }

    private void initListeners() {

	    EventDispatcher dispatcher = client.getEventDispatcher();
	    listener = new Listener(this);
	    dispatcher.on(MessageDeleteEvent.class).subscribe(listener::onDelete);
	    dispatcher.on(ReadyEvent.class).subscribe(listener::onReadyEvent);
	    dispatcher.on(GuildCreateEvent.class).subscribe(listener::onJoinServer);
	    dispatcher.on(MessageCreateEvent.class).subscribe(listener::onMessageReceivedEvent);

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

    public void addCommands(ICommand... ICommands){
        listener.addCommands(ICommands);
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
	    Main.exit();
    }
    
    private DiscordClient createClient(String token) {
        ClientBuilder clientBuilder = new ClientBuilder(token);
        return clientBuilder.build();
    }
}
