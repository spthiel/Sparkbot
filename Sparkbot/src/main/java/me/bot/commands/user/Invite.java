package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
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

    private static Permission[] PERMISSIONS = new Permission[]{};

    @Override
    public Permission[] getRequiredPermissions() {
        return PERMISSIONS;
    }
    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    private String LINK = "http://bit.ly/invSparkbot";

    @Override
    public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {
       channel.createMessage(new MessageCreateSpec().setContent("Hello, <@" + author.getId().asLong() + "> you can invite me with <" + LINK + ">")).subscribe();

    }

    @Override
    public void onLoad() {

    }
}
