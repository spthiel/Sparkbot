package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageEditSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import discord4j.discordjson.possible.Possible;
import discord4j.rest.util.Permission;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
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
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
        Instant invoker = message.getTimestamp();
        channel.createMessage("**Pong!**").subscribe(
            message1 -> {
                Instant messageTimestamp = message1.getTimestamp();
                long delay = messageTimestamp.toEpochMilli()-invoker.toEpochMilli();

                message1.edit(editSpec("**Pong!** Hey " + author.getMention() + " it took me " + delay + "ms to read your message.")).subscribe(
                        message2 -> {
                            Instant edited = message2.getEditedTimestamp().orElse(null);
                            if(edited != null) {
                                long delay2 = edited.toEpochMilli()-messageTimestamp.toEpochMilli();
                                message2.edit(editSpec("**Pong!** Hey " + author.getMention() + " it took me **" + delay + "**ms to read your message and **" + delay2 + "**ms to edit it again.")).subscribe();
                            }
                        }
                );
            });

    }
    
    private MessageEditSpec editSpec(String content) {
        return MessageEditSpec.builder().content(Possible.of(Optional.of(content))).build();
    }

    @Override
    public void onLoad(final Bot bot) {

    }
}
