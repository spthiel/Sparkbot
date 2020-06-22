package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.List;

public class Remotelogout implements ICommand {


    @Override
    public CommandType getType() {
        return CommandType.ADMIN;
    }

    @Override
    public String getHelp() {
        return "Remote logout of all instances";
    }

    @Override
    public String[] getNames() {
        return new String[]{"logout"};
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
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
        bot.disable();
    }

    @Override
    public void onLoad(final Bot bot) {

    }
}
