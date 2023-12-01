package me.bot.commands.admin;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

@SuppressWarnings("unused")
public class SubmitBabamote implements ICommand {
    
    private Bot bot;
    
    private Mono<TextChannel> getEmoteChannel() {
        if(bot == null) {
            return Mono.empty();
        }
        return bot.getGateway().getChannelById(Snowflake.of(584408017404166144L)).filter(channel -> channel instanceof TextChannel).map(channel -> (TextChannel)channel);
    }
    
    @Override
    public CommandType getType() {
        
        return CommandType.ADMIN;
    }
    
    @Override
    public String getHelp() {
        
        return "Adds new emotes to babamote emote channel";
    }
    
    @Override
    public String[] getNames() {
        
        return new String[]{"babamote"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return new String[]{Prefixes.getSuperAdminPrefix()};
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
        if(args.length >= 1 && args[0].matches("<a?:.+?:(\\d+)>")) {
            String emoji = args[0];
            final String actualEmoji = emoji.replaceAll("<a?:.+?:(\\d+)>", "$1");
            final String emojiName = emoji.replaceAll("<a?:(.+?):\\d+>", "$1");
            final String format = emoji.matches("<a:.+?:(\\d+)>") ? "gif" : "png";
            getEmoteChannel()
                    .subscribe(c -> c
                    .createMessage(createEmbed(actualEmoji, emojiName, format))
                    .subscribe());
        }
    }
    
    private final Color color = Color.of(255, 142, 228);
    
    private EmbedCreateSpec createEmbed(String emojiId, String emojiName, String format) {
        
        return EmbedCreateSpec.builder()
        .title(":" + emojiName + ":")
        .color(color)
        .thumbnail("https://cdn.discordapp.com/emojis/" + emojiId + "." + format + "?v=1").build();
        
    }
    
    @Override
    public void onLoad(Bot bot) {
        this.bot = bot;
    }
}
