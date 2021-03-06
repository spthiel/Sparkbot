package me.bot.commands.admin;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

public class Fix implements ICommand {
	
	@Override
	public CommandType getType() {
		
		return CommandType.ADMIN;
	}
	
	@Override
	public String getHelp() {
		
		return "Fix stupid stuff";
	}
	
	@Override
	public String[] getNames() {
		
		return new String[]{"fixme"};
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
	public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
		
		channel.createMessage(spec -> spec.setContent("It was working.. why u do dis to me?")).subscribe();
	}
	
	@Override
	public void onLoad(Bot bot) {
		
		bot.getGateway()
			.on(MessageCreateEvent.class)
			.subscribe(
				event ->
					event.getMessage().getChannel()
						.filter(messageChannel -> event.getMember().isPresent() && !event.getMember().get().isBot())
						.subscribe(
							objects -> {
								Member member = event.getMember().get();
								if(member.getId().asString().equalsIgnoreCase("261538420952662016") &&
									event.getMessage().getContent().equalsIgnoreCase("s$fixme")) {
									bot.setupMessageCreateListener();
								}
							}
						)
				,
				Throwable:: printStackTrace
			);
	}
}
