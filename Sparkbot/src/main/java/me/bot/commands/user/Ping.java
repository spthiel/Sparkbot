package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

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

    private static Permission[] PERMISSIONS = new Permission[]{};

    @Override
    public Permission[] getRequiredPermissions() {
        return PERMISSIONS;
    }
    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {
        ZonedDateTime zdt = message.getTimestamp().atZone(ZoneOffset.UTC);
        channel.createMessage(new MessageCreateSpec().setContent("**Pong!**")).subscribe(
            message1 -> {
                ZonedDateTime now = message1.getTimestamp().atZone(ZoneOffset.UTC);
                long delay = now.toInstant().toEpochMilli()-zdt.toInstant().toEpochMilli();

                message1.edit(new MessageEditSpec().setContent("**Pong!** Hey <@" + author.getId().asLong() + "> it took me " + delay + "ms to read your message.")).subscribe();
            });

    }

    @Override
    public void onLoad() {

    }
}
