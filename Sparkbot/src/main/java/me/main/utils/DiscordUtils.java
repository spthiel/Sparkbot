package me.main.utils;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.*;

import me.bot.base.Bot;

import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscordUtils {
	
	private Bot bot;
	
	public DiscordUtils(Bot bot) {
		
		this.bot = bot;
	}
	
	public static MessageChannel getMessageChannelOfChannel(Channel channel) {
		
		if (channel instanceof MessageChannel) {
			return (MessageChannel) channel;
		}
		return null;
	}
	
	public static TextChannel getTextChannelOfChannel(Channel channel) {
		
		if (channel instanceof TextChannel) {
			return (TextChannel) channel;
		}
		return null;
	}
	
	private static Optional<Member> getArgMember(List<Member> members, String arg) {
		
		Member contain            = null;
		Member containInsensitive = null;
		for (Member member : members) {
			String discrim    = "#" + member.getDiscriminator();
			String normalname = member.getUsername();
			String name       = member.getDisplayName();
			if ((normalname + discrim).equalsIgnoreCase(arg) || (name + discrim).equalsIgnoreCase(arg)) {
				return Optional.of(member);
			}
			
			if (normalname.equalsIgnoreCase(arg) || name.equalsIgnoreCase(arg)) {
				return Optional.of(member);
			}
			
			if (normalname.contains(arg) || name.contains(arg)) {
				if (contain == null) {
					contain = member;
				}
			} else if (normalname.toLowerCase().contains(arg.toLowerCase()) || name
					.toLowerCase()
					.contains(arg.toLowerCase())) {
				if (containInsensitive == null) {
					containInsensitive = member;
				}
			}
		}
		if (contain != null) {
			return Optional.of(contain);
		} else if (containInsensitive != null) {
			return Optional.of(containInsensitive);
		} else {
			return Optional.empty();
		}
	}
	
	public static Mono<Member> getMember(Bot bot, Guild guild, String arg) {
		
		if (arg.matches("<@(\\d{8,})>")) {
			String id = arg.replaceAll("<@(\\d+)>", "$1");
			return bot.getGateway().getMemberById(guild.getId(), Snowflake.of(id));
		} else if (arg.matches("\\d{8,}")) {
			return bot.getGateway().getMemberById(guild.getId(), Snowflake.of(arg));
		} else {
			return guild.getMembers().collectList().flatMap(members -> Mono.justOrEmpty(getArgMember(members, arg)));
		}
	}
	
	public static Mono<Optional<Role>> getRoleByName(Bot bot, Guild guild, String rolename) {
		
		String lcase = rolename.toLowerCase();
		return guild.getRoles().filter(role -> role.getName().equalsIgnoreCase(rolename.replace('_', ' ')))
					.switchIfEmpty(guild.getRoles().filter(role -> role.getName().toLowerCase().startsWith(lcase)))
					.map(Optional :: of)
					.defaultIfEmpty(Optional.empty()).single();
	}
	
	public static Mono<Optional<Role>> getRoleById(Bot bot, Guild guild, String id) {
		
		return guild.getRoleById(Snowflake.of(id)).map(Optional :: of).defaultIfEmpty(Optional.empty());
	}
	
	public static Flux<Member> getRoleMembers(Guild guild, Optional<Role> oRole, final long count) {
		
		if (!oRole.isPresent()) {
			return Flux.empty();
		}
		Role role = oRole.get();
		return guild.getMembers().filterWhen(member -> member.getRoles().any(r -> r.equals(role)))
					.take(count);
	}
	
	public static Flux<Member> getRoleMembers(Guild guild, Role role, final long count) {
		
		return guild.getMembers().filterWhen(member -> member.getRoles().any(r -> r.equals(role)))
					.take(count);
	}
	
}
