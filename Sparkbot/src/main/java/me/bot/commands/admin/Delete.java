package me.bot.commands.admin;

import me.bot.base.Bot;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;

public class Delete implements ICommand {
    @Override
    public String getHelp() {
        return "Deletes messages in bulk";
    }

    @Override
    public String[] getNames() {
        String[] out = {"clear", "prune"};
        return out;
    }

    @Override
    public String[] getPrefixes(IGuild guild) {
        return Prefixes.getAdminPrefixesFor(guild);
    }

    @Override
    public boolean hasPermissions(IGuild guild, IUser user) {
        EnumSet<Permissions> perms = user.getPermissionsForGuild(guild);
        return perms.contains(Permissions.ADMINISTRATOR) || perms.contains(Permissions.MANAGE_MESSAGES);
    }

    @Override
    public List<Permissions> requiredBotPermissions() {
        List<Permissions> out = new ArrayList<Permissions>();
        out.add(Permissions.MANAGE_MESSAGES);
        return out;
    }

    @Override
    public void run(Bot bot, IUser author, IMessage message, String[] args) {

        EnumSet<Permissions> botperms = bot.getClient().getOurUser().getPermissionsForGuild(message.getGuild());
        if (!botperms.contains(Permissions.ADMINISTRATOR) || !botperms.contains(Permissions.MANAGE_MESSAGES)) {
            MessageAPI.sendAndDeleteMessageLater(message.getChannel(),"<:red_cross:398120014974287873> **| I need `MANAGE_MESSAGES` permissions to perfom that command.**",10000L);
            return;
        }

        if (args.length > 1) {
            try {
                int num = Integer.parseInt(args[1]);
                if (num > 999)
                    num = 999;
                final int amount = num;
                RequestBuffer.request(() -> {
                    message.getChannel().getMessageHistory(amount+1).bulkDelete();
                }).get();
                MessageAPI.sendAndDeleteMessageLater(message.getChannel(), ":white_check_mark: **| Successfully deleted " + amount + " messages from <#" + message.getChannel().getLongID() + "> **", 10000L);
            } catch (Exception e) {
                MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "<:red_cross:398120014974287873> **|" + args[1] + " is not a valid number.**", 5000L);
            }
        } else {
            MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "<:red_cross:398120014974287873> **| Please enter the amount of messages you want to delete.**\nExample: `" + Prefixes.getAdminPrefixFor(message.getGuild()) + "clear 10`", 5000L);
        }
    }

    @Override
    public void onLoad() {

    }
}
