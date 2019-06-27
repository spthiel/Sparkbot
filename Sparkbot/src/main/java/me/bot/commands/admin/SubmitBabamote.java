package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

public class SubmitBabamote implements ICommand {
    
    private Bot bot;
    
    private Mono<TextChannel> getEmotechannel() {
        if(bot == null) {
            return Mono.empty();
        }
        return bot.getClient().getChannelById(Snowflake.of(584408017404166144L)).filter(channel -> channel instanceof TextChannel).map(channel -> (TextChannel)channel);
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
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
        if(args.length >= 1 && args[0].matches("<a?:.+?:(\\d+)>")) {
            String emoji = args[0];
            final String actualemoji = emoji.replaceAll("<a?:.+?:(\\d+)>", "$1");
            final String emojiname = emoji.replaceAll("<a?:(.+?):\\d+>", "$1");
            final String fileformat = emoji.matches("<a:.+?:(\\d+)>") ? "gif" : "png";
            getEmotechannel()
                    .subscribe(c -> c
                    .createMessage(s -> s.setEmbed(spec -> createEmbed(spec, actualemoji, emojiname, fileformat)))
                    .subscribe());
        }
    }
    
    private final Color color = new Color(255, 142, 228);
    
    private void createEmbed(EmbedCreateSpec spec, String emojiid, String emojiname, String fileformat) {
        
        spec.setTitle(":" + emojiname + ":");
        spec.setColor(color);
        spec.setThumbnail("https://cdn.discordapp.com/emojis/" + emojiid + "." + fileformat + "?v=1");
        
    }
    
    @Override
    public void onLoad(Bot bot) {
        this.bot = bot;
    }
}
