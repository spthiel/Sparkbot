package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Image;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings("unused")
public class Stats implements ICommand {
	
	@Override
	public CommandType getType() {
		
		return CommandType.PUBLIC;
	}
	
	@Override
	public String getHelp() {
		
		return "Displays Stats";
	}
	
	@Override
	public String[] getNames() {
		
		return new String[]{"stats", "info"};
	}
	
	@Override
	public String[] getPrefixes(Guild guild) {
		
		return Prefixes.getNormalPrefixesFor(guild);
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
		
		bot.getClient()
		   .getGuilds()
		   .count()
		   .subscribe(
			   guildCount -> {
				   Instant joinTime      = guild.getJoinTime();
				   String  formattedTime = DateTimeFormatter.ofPattern("hh:mm:ss a dd.MMM.yyyy")
															.withZone(ZoneId.of("UTC"))
															.format(joinTime);
				   bot.getGateway()
					  .getSelf()
					  .subscribe(
						  self -> {
							  String avatarUrl = self.getAvatarUrl(Image.Format.PNG)
													 .orElse("");
							  channel.createMessage(getEmbed(avatarUrl, guildCount, formattedTime, uptime(bot)))
									 .subscribe();
						  }
					  );
			   });
	}
	
	private EmbedCreateSpec getEmbed(String avatarURL, Long guildCount, String formattedTime, String uptime) {
		
		return EmbedCreateSpec
			.builder()
			.color(Color.of(0xDC143C))
			.thumbnail(avatarURL)
			.addField("Creator", "xelspeth", true)
			.addField("Guilds", guildCount + "", true)
			.addField("Join time", formattedTime, true)
			.addField("Uptime", uptime, true)
			.build()
			;
	}
	
	private String uptime(Bot bot) {
		
		long start = bot.getStartTime();
		long now   = System.currentTimeMillis();
		int  diff  = (int) ((now - start) / 1000);
		
		int s, m, h, d;
		
		s = diff % 60;
		diff = (diff - s) / 60;
		m = diff % 60;
		diff = (diff - m) / 60;
		h = diff % 24;
		diff = (diff - h) / 24;
		d = diff;
		return formatTime(d, h, m, s);
	}
	
	private String formatTime(int days, int hour, int minutes, int seconds) {
		
		return (days > 0 ? days + "d " : "") + (hour > 0 ? hour + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s " : "");
	}
	
	@Override
	public void onLoad(final Bot bot) {
	
	}
}
