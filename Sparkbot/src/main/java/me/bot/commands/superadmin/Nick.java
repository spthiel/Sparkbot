package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

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
		String[] names = {"nick"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		String[] prefixes = {Prefixes.getSuperAdminPrefix()};
		return prefixes;
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		List<Permissions> out = new ArrayList<Permissions>();
		out.add(Permissions.MANAGE_NICKNAMES);
		return out;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		if(args.length > 1) {
			StringBuilder newname = new StringBuilder();
			for(int i = 1; i < args.length; i++)
				newname.append(args[i] + " ");

			String name = newname.toString().trim();
			message.getGuild().setUserNickname(author,name);
			MessageBuilder builder = new MessageBuilder(bot.getClient());


			builder
					.withChannel(message.getChannel())
					.appendContent("Look " + author.getName() + " you have a new Name! Hi, " + name +" :wave:");
			MessageAPI.sendMessage(builder);

		}
	}

	@Override
	public void onLoad() {

	}
}
