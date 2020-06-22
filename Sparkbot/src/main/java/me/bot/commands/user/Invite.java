package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.util.List;

public class Invite implements ICommand {

    @Override
    public CommandType getType() {
        return CommandType.PUBLIC;
    }

    @Override
    public String getHelp() {
        return "Get's the link to Invite the bot to your Server";
    }

    @Override
    public String[] getNames() {
	    return new String[]{"invite","link"};
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

    private static String LINK = "http://bit.ly/invSparkbot";

    @Override
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
       channel.createMessage(spec -> spec.setContent("Hello, <@" + author.getId().asLong() + "> you can invite me with <" + LINK + ">")).subscribe();

    }

    @Override
    public void onLoad(final Bot bot) {

    }
}
