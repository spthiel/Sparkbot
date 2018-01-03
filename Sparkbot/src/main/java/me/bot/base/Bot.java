package me.bot.base;

import me.bot.base.configs.ResourceManager;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.util.List;

public class Bot {
    
    private IDiscordClient client;
    public Discord4J.Discord4JLogger LOGGER = (Discord4J.Discord4JLogger) Discord4J.LOGGER;
    private CommandListener listener;
    private String name;
    private String url;
    private boolean streaming;
    private ResourceManager resourceManager;

    public Bot(String token, String name, String basefolder) {

        this.name = name;
        this.streaming = false;

        this.resourceManager = new ResourceManager(basefolder);

        client = createClient(token, true);
        EventDispatcher dispatcher = client.getDispatcher();
        listener = new CommandListener(this);
        dispatcher.registerListener(listener);

    }

    public Bot(String token, String name, String basefolder, String streamingurl) {

        this.name = name;
        this.streaming = true;
        this.url = streamingurl;

        this.resourceManager = new ResourceManager(basefolder);

        client = createClient(token, true);
        EventDispatcher dispatcher = client.getDispatcher();
        listener = new CommandListener(this);
        dispatcher.registerListener(listener);

    }

    public boolean isStreaming() {
        return streaming;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public String getUrl() {
        return url;
    }

    public void addCommands(ICommand... commands){
        listener.addCommands(commands);
    }

    public List<ICommand> getCommands(){
        return listener.getCommands();
    }

    public String getName(){
        return name;
    }
    
    public IDiscordClient getClient() {
        return client;
    }
    
    public void disable() {
        LOGGER.setLevel(Discord4J.Discord4JLogger.Level.INFO);
        LOGGER.info("Disabling");
        if (client != null && client.isLoggedIn())
            client.logout();
    }
    
    private IDiscordClient createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token)
                .withRecommendedShardCount();
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }
    }
}
