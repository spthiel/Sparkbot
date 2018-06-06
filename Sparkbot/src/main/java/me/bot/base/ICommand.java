package me.bot.base;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.main.PermissionManager;
import reactor.core.publisher.Mono;

import java.util.EnumSet;
import java.util.List;

public interface ICommand {

	default Mono<Boolean> hasPermission(Guild guild, User author) {
		if (PermissionManager.isBotAdmin(author))
			return Mono.just(true);
		else if(getType() != CommandType.ADMIN)
			return hasGuildPermissions(guild, author);
		else
			return Mono.just(false);
	}

	default Mono<Boolean> hasGuildPermissions(Guild guild, User author) {
		return PermissionManager.getPermissions(guild,author)
				.filter(permissions -> permissions.contains(Permission.ADMINISTRATOR) || permissions.containsAll(requiredBotPermissions()))
				.hasElement();
	}

	CommandType getType();
	String getHelp();
	String[] getNames();
	String[] getPrefixes(final Guild guild);
	Permission[] getRequiredPermissions();
	List<Permission> requiredBotPermissions();
	void run(final Bot bot, final User author, final TextChannel channel, final Guild guild, final Message message, final String commandname, final String[] args, final String content);
	void onLoad();

}
