package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class Ping implements ICommand {
    @Override
    public CommandType getType() {
        return CommandType.PUBLIC;
    }

    @Override
    public String getHelp() {
        return "Get the time it takes the Bot to answer your command.";
    }

    @Override
    public String[] getNames() {
	    return new String[]{"ping"};
    }

    @Override
    public String[] getPrefixes(Guild guild) {
        return Prefixes.getNormalPrefixesFor(guild);
    }

    @Override
    public boolean hasPermissions(User user, Guild guild) {
        return true;
    }

    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
        ZonedDateTime zdt = message.getTimestamp().atZone(ZoneOffset.UTC);
        Message message1 = channel.createMessage(new MessageCreateSpec().setContent("**Ping!**")).block();

        ZonedDateTime now = message1.getTimestamp().atZone(ZoneOffset.UTC);
        long delay = now.toInstant().toEpochMilli()-zdt.toInstant().toEpochMilli();

        message1.edit(new MessageEditSpec().setContent("**Ping!** Hey <@" + author.getId().asLong() + "> it took me " + delay + "ms to read your message."));
    }

    @Override
    public void onLoad() {

    }
}
