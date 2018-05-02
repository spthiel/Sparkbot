package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

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
    public boolean hasPermissions(User user, Guild guild) {
        return true;
    }

    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    private String LINK = "http://bit.ly/invSparkbot";

    @Override
    public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
       channel.createMessage(new MessageCreateSpec().setContent("Hello, <@" + author.getId().asLong() + "> you can invite me with " + LINK));

    }

    @Override
    public void onLoad() {

    }
}
