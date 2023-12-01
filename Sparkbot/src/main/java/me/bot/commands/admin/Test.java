package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Test implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Test command";
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
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		
		channel.createMessage(mspec -> mspec.addEmbed(spec -> spec
				.setColor(Color.of(0x2409E9))
				.addField("Command", commandName, true)
				.addField("Args", Arrays.toString(args),true)
				.addField("Content","`" + content + "`",true)
				.addField("Channel",channel.getName(),true)
				.addField("Guild",guild.getName(),true)
				.addField("Author", "<@" + author.getId().asLong() + ">",true)
				.addField("Author name:", author.getUsername(),true)
				.addField("Author display name:", author.getDisplayName(),true)
				.addField("Author Nickname:", author.getNickname().orElse("null"),true))
		).subscribe();

		
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
