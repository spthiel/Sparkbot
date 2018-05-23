package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.HTTP;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Test implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Testcommand";
	}

	@Override
	public String[] getNames() {
		return new String[]{"test"};
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
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, Message message, String command, String[] args, String content) {

		channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec()
				.addField("Command",command,true)
				.addField("Args", Arrays.toString(args),true)
				.addField("Content",content,true)
				.addField("Channel",((GuildChannel)channel).getName(),true)
				.addField("Guild",guild.getName(),true)
				.addField("Author", "<@" + author.getId().asLong() + ">",true))
		).subscribe();
	}

	@Override
	public void onLoad() {

	}
}
