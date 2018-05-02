package me.bot.commands.superadmin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.*;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

public class Nick implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Let you change your nick";
	}

	@Override
	public String[] getNames() {
		return new String[]{"nick"};
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
		List<Permission> out = new ArrayList<>();
		out.add(Permission.MANAGE_NICKNAMES);
		return out;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
		if(args.length > 1) {
			StringBuilder newname = new StringBuilder();
			for(int i = 1; i < args.length; i++)
				newname.append(args[i] + " ");

			String name = newname.toString().trim();
//			message.getGuild().setUserNickname(author,name);
			MessageBuilder builder = new MessageBuilder(bot.getClient());

			builder
					.withChannel(channel)
					.appendContent("Look " + author.getUsername() + " you have a new Name! Hi, " + name +" :wave:");
			builder.send();

		}
	}

	@Override
	public void onLoad() {

	}
}
