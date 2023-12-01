package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import discord4j.rest.util.Permission;

import java.util.List;

@SuppressWarnings("unused")
public class Invite implements ICommand {

    @Override
    public CommandType getType() {
        return CommandType.PUBLIC;
    }

    @Override
    public String getHelp() {
        return "Gets the link to Invite the bot to your Server";
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

    private static final String LINK = "https://bit.ly/invSparkbot";

    @Override
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
       channel.createMessage("Hello, " + author.getMention() + " you can invite me with <" + LINK + ">").subscribe();

    }

    @Override
    public void onLoad(final Bot bot) {

    }
}
