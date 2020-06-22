package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.time.Instant;
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
	public List<Permission> getRequiredPermissions() {
		return null;
	}

    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
        Instant invoker = message.getTimestamp();
        channel.createMessage(spec -> spec.setContent("**Pong!**")).subscribe(
            message1 -> {
                Instant messgeTimestamp = message1.getTimestamp();
                long delay = messgeTimestamp.toEpochMilli()-invoker.toEpochMilli();

                message1.edit(spec -> spec.setContent("**Pong!** Hey <@" + author.getId().asLong() + "> it took me " + delay + "ms to read your message.")).subscribe(
                        message2 -> {
                            Instant edited = message2.getEditedTimestamp().orElse(null);
                            if(edited != null) {
                                long delay2 = edited.toEpochMilli()-messgeTimestamp.toEpochMilli();
                                message2.edit(spec -> spec.setContent("**Pong!** Hey <@" + author.getId().asLong() + "> it took me **" + delay + "**ms to read your message and **" + delay2 + "**ms to edit it again.")).subscribe();
                            }
                        }
                );
            });

    }

    @Override
    public void onLoad(final Bot bot) {

    }
}
