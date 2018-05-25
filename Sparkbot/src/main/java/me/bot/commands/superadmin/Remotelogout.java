package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

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
        bot.disable();
    }

    @Override
    public void onLoad() {

    }
}
