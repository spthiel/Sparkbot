package me.bot.base;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.configs.PermissionManager;
import reactor.core.publisher.Mono;

import java.security.Permissions;
import java.util.List;

public interface ICommand {

	default Mono<Boolean> hasPermission(Bot bot, Guild guild, Member author) {

		if (bot.getPermissionManager().isBotAdmin(author))
			return Mono.just(true);
		else if(getType() != CommandType.ADMIN)
			return hasGuildPermissions(bot, guild, author);
		else
			return Mono.just(false);
	}

	default Mono<Boolean> hasGuildPermissions(Bot bot, Guild guild, Member author) {
		final List<Permission> perms = getRequiredPermissions();
		if(perms == null) {
			return Mono.just(true);
		}
		
		return bot.getPermissionManager().getPermissions(guild,author)
			.filter(permissions -> permissions.contains(Permission.ADMINISTRATOR) || permissions.containsAll(perms))
			.hasElement();
	}

	CommandType getType();
	String getHelp();
	String[] getNames();
	String[] getPrefixes(final Guild guild);
	List<Permission> getRequiredPermissions();
	List<Permission> requiredBotPermissions();
	void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandname, final String[] args, final String content);
	void onLoad(final Bot bot);

}
