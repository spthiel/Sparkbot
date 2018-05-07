package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Delete implements ICommand {

	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
    public String getHelp() {
        return "Deletes messages in bulk";
    }

    @Override
    public String[] getNames() {
        return new String[]{"clear", "prune"};
    }

    @Override
    public String[] getPrefixes(Guild guild) {
        return Prefixes.getAdminPrefixesFor(guild);
    }

    @Override
    public boolean hasPermissions(User user, Guild guild) {
	    EnumSet<Permission> perms = PermissionManager.getPermissions(guild,user).block();
	    if(perms != null)
		    return perms.contains(Permission.MANAGE_GUILD) || perms.contains(Permission.ADMINISTRATOR);
	    else
		    return false;
    }

    @Override
    public List<Permission> requiredBotPermissions() {
        List<Permission> out = new ArrayList<>();
        out.add(Permission.MANAGE_MESSAGES);
        return out;
    }

    @Override
    public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {

		//TODO: Fix stuff

//        if (args.length > 1) {
//            if(args[1].equalsIgnoreCase("channel")) {
//
//
//	            List<Message> deletedMessages = RequestBuffer.request(() -> {
//		            return message.getChannel().bulkDelete();
//	            }).get();
//	            MessageAPI.sendAndDeleteMessageLater(message.getChannel(), ":white_check_mark: **| Successfully deleted " + (deletedMessages.size()-1) + " messages from <#" + message.getChannel().getLongID() + "> **", 10000L);
//
//            } else {
//
//	            try {
//		            int num = Integer.parseInt(args[1]);
//		            if (num > 100)
//			            num = 100;
//		            final int amount = num;
//		            RequestBuffer.request(() -> {
//			            message.delete();
//		            });
//		            List<IMessage> deletedMessages = RequestBuffer.request(() -> {
//			            return message.getChannel().getMessageHistory(amount).bulkDelete();
//		            }).get();
//		            MessageAPI.sendAndDeleteMessageLater(message.getChannel(), ":white_check_mark: **| Successfully deleted " + (deletedMessages.size()) + " messages from <#" + message.getChannel().getLongID() + "> **", 10000L);
//	            } catch (Exception e) {
//		            MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "<:red_cross:398120014974287873> **|" + args[1] + " is not a valid number.**", 5000L);
//	            }
//            }
//        } else {
//            MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "<:red_cross:398120014974287873> **| Please enter the amount of messages you want to delete.**\nExample: `" + Prefixes.getAdminPrefixFor(message.getGuild()) + "clear 10`", 5000L);
//        }
    }

    @Override
    public void onLoad() {

    }
}
