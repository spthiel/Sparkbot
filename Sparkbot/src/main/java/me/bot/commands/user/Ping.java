package me.bot.commands.user;

import me.bot.base.Bot;
import me.bot.base.ICommand;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.RequestBuffer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class Ping implements ICommand{
    @Override
    public String getHelp() {
        return "Get the time it takes the Bot to answer your command.";
    }

    @Override
    public String[] getNames() {
        String[] names = {"ping"};
        return names;
    }

    @Override
    public String[] getPrefixes(IGuild guild) {
        return Prefixes.getNormalPrefixesFor(guild);
    }

    @Override
    public boolean hasPermissions(IGuild guild, IUser user) {
        return true;
    }

    @Override
    public List<Permissions> requiredBotPermissions() {
        return null;
    }

    @Override
    public void run(Bot bot, IUser author, IMessage message, String[] args) {
        ZonedDateTime zdt = message.getTimestamp().atZone(ZoneOffset.UTC);
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneOffset.UTC);
        long delay = now.toInstant().toEpochMilli()-zdt.toInstant().toEpochMilli();
        RequestBuffer.request(() -> {
           message.getChannel().sendMessage("Hey <@" + author.getLongID() + "> it took me " + delay + "ms to read your message.");
        });
    }

    @Override
    public void onLoad() {

    }
}
