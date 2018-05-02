package me.bot.commands.superadmin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
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
    public boolean hasPermissions(User user, Guild guild) {
        return PermissionManager.isBotAdmin(user);
    }

    @Override
    public List<Permission> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
        bot.disable();
    }

    @Override
    public void onLoad() {

    }
}
