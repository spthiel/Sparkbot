package me.bot.base;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public interface ICommand {
    
    String getHelp();
    String[] getNames();
    String[] getPrefixes(IGuild guild);
    boolean hasPermissions(IGuild guild,IUser user);
    List<Permissions> requiredBotPermissions();
    void run(Bot bot, IUser author, IMessage message, String[] args);
    void onLoad();
    
}
