package me.bot.base;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.configs.PermissionManager;
import me.main.Main;

import reactor.core.publisher.Mono;

import java.security.Permissions;
import java.util.List;

public interface ICommand {
	
	
	default Mono<Boolean> hasPermission(Bot bot, Guild guild, Member author) {
		
		if (bot.getPermissionManager().isBotAdmin(author))
			return Mono.just(true);
		if(Main.testInstance) {
			return Mono.just(false);
		}
		if(getType() != CommandType.ADMIN)
			return PermissionManager.hasGuildPermissions(author, getRequiredPermissions());
		else
			return Mono.just(false);
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
