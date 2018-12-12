package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.awt.*;
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
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {

		channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec()
				.setColor(new Color(colorToInt("#2409E9")))
				.addField("Command",command,true)
				.addField("Args", Arrays.toString(args),true)
				.addField("Content","`" + content + "`",true)
				.addField("Channel",channel.getName(),true)
				.addField("Guild",guild.getName(),true)
				.addField("Author", "<@" + author.getId().asLong() + ">",true)
				.addField("Author name:", author.getUsername(),true)
				.addField("Author Nickname:", author.getNickname().orElse("null"),true))
		).subscribe();
		
		MessageCreateSpec spec = new MessageCreateSpec();
		StringBuilder out = new StringBuilder();
		
//		for(int i = 0x10000; i < 0x10100; i++) {
//			out.append(Character.toChars(i));
//		}
		
		out.append(Character.toChars(0x1F970));
		System.out.println(out.toString());
		spec.setContent(out.toString());
		channel.createMessage(spec).subscribe();
		
	}

	private int colorToInt(String colorhex) {
		return Integer.parseInt(colorhex.replace("#",""),16);
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
