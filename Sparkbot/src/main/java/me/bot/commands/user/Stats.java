package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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
		return new String[]{"stats","info"};
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

	private static String AVATAR_URL = "https://cdn.discordapp.com/avatars/%ID%/%HASH%.png?size=2048";

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
		bot.getClient().getGuilds().count().subscribe(
			guildcount ->{
				Instant jointime = guild.getJoinTime().orElse(null);
				channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec()
						.setColor(colorToInt("#DC143C"))
						.setThumbnail(AVATAR_URL.replace("%ID%", bot.getBotuser().getId().asLong() + "").replace("%HASH%", bot.getBotuser().getAvatarHash().orElse("")))
						.addField("Creator", "spthiel#1317", true)
						.addField("Guilds", guildcount + "", true)
						.addField("Join time", jointime == null ? "Error" : jointime.toString(), true)
						.addField("Uptime",uptime(bot),true)
					)
				).subscribe();
			});
	}

	private int colorToInt(String colorhex) {
		return Integer.parseInt(colorhex.replace("#",""),16);
	}

	private String uptime(Bot bot) {
		long start = bot.getStartTime();
		long now = System.currentTimeMillis();
		int diff = (int)((now-start)/1000);

		int s,m,h,d;

		s = diff%60;
		diff = (diff-s)/60;
		m = diff%60;
		diff = (diff-m)/60;
		h = diff%24;
		diff = (diff-h)/24;
		d = diff;
		return formatTime(d,h,m,s);
	}

	private String addLeadingZero(int num) {
		if(num < 10 && num > 0)
			return 0 + "" + num;
		else
			return num + "";
	}

	private String formatTime(int days, int hour, int minutes, int seconds) {
		return (days > 0 ? days + "d " : "") + (hour > 0 ? hour + "h " : "") + (minutes > 0 ? minutes + "m " : "") + (seconds > 0 ? seconds + "s " : "");
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
