package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;
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

	private static Permission[] PERMISSIONS = new Permission[]{};

	@Override
	public Permission[] getRequiredPermissions() {
		return PERMISSIONS;
	}
	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {

		channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec()
				.setColor(ColorToInt("#2409E9"))
				.addField("Command",command,true)
				.addField("Args", Arrays.toString(args),true)
				.addField("Content",content,true)
				.addField("Channel",channel.getName(),true)
				.addField("Guild",guild.getName(),true)
				.addField("Author", "<@" + author.getId().asLong() + ">",true))
		).subscribe();
	}

	private int ColorToInt(String colorhex) {
		return Integer.parseInt(colorhex.replace("#",""),16);
	}

	@Override
	public void onLoad() {

	}
}
