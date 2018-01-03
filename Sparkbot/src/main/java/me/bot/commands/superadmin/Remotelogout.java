package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.ICommand;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public class Remotelogout implements ICommand{


    @Override
    public String getHelp() {
        return "Remote logout of all instances";
    }

    @Override
    public String[] getNames() {
        String[] names = {"logout"};
        return names;
    }

    @Override
    public String[] getPrefixes(IGuild guild) {
        String[] prefixes = {Prefixes.getSuperAdminPrefix()};
        return prefixes;
    }

    @Override
    public boolean hasPermissions(IGuild guild, IUser user) {
        return false;
    }

    @Override
    public List<Permissions> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(Bot bot, IUser author, IMessage message, String[] args) {

    }

    @Override
    public void onLoad() {

    }
}
