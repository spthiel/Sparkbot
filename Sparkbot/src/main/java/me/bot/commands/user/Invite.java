package me.bot.commands.user;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.RequestBuffer;

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
        String[] names = {"invite","link"};
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

    private String LINK = "http://bit.ly/invSparkbot";

    @Override
    public void run(Bot bot, IUser author, IMessage message, String[] args) {
        RequestBuffer.request(() -> {
           message.getChannel().sendMessage("Hello, <@" + author.getLongID() + "> you can invite me with " + LINK);
        });

    }

    @Override
    public void onLoad() {

    }
}
