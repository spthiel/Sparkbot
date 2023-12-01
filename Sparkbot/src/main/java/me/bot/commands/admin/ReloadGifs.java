package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.commands.user.Gif;
import me.main.Prefixes;

import java.util.List;

@SuppressWarnings("unused")
public class ReloadGifs implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Reloads the gifs";
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public String[] getNames() {
		return new String[]{"reloadgifs","rlgifs"};
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
	public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandName, String[] args, String content) {
		Gif.getInstance().loadGifManager(bot);
		channel.createMessage("Reloaded gif managers").subscribe();
	}

	@Override
	public void onLoad(Bot bot) {

	}
}
