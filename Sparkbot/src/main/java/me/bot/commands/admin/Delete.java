package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.PermissionManager;
import me.main.Prefixes;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (args.length > 1) {
            if(args[1].equalsIgnoreCase("channel")) {

	            System.out.println("works");
				TextChannel c = (TextChannel) channel;
	            final int[] amount = {0};
	            message.delete().block();
	            Flux<Snowflake> messages = c.getMessagesBefore(message.getId()).map(Message::getId);
	            messages.count().subscribe((result) -> {
	            	amount[0]+=result;
		            c.bulkDelete(messages).subscribe(
			            ignored -> amount[0]--,
			            Throwable::printStackTrace,
			            () -> MessageAPI.sendAndDeleteMessageLater(channel, ":white_check_mark: **| Successfully deleted " + (amount[0]) + " messages from <#" + c.getId().asLong() + "> **", 10000L)
					);
	            });


            } else {

	            try {
		            System.out.println(Arrays.toString(args));
		            int num = Integer.parseInt(args[1]);
		            if (num > 1000)
			            num = 1000;
		            TextChannel c = (TextChannel) channel;
		            final int[] amount = {num};
		            message.delete().block();
		            c.bulkDelete(c.getMessagesBefore(message.getId()).take(num).map(Message::getId)).subscribe((ignored) -> amount[0]--);
		            MessageAPI.sendAndDeleteMessageLater(channel, ":white_check_mark: **| Successfully deleted " + (amount[0]) + " messages from <#" + c.getId().asLong() + "> **", 10000L);
	            } catch (NumberFormatException e) {
		            MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **|" + args[1] + " is not a valid number.**", 5000L);
	            }
            }
        } else {
            MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| Please enter the amount of messages you want to delete.**\nExample: `" + Prefixes.getAdminPrefixFor(guild) + "clear 10`", 5000L);
        }
    }

    @Override
    public void onLoad() {

    }
}
