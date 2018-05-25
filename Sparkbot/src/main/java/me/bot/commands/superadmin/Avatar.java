package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

import java.util.List;

public class Avatar implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Changes bot's Avatar";
	}

	@Override
	public String[] getNames() {
		return new String[]{"avatar","changeavatar"};
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
		if(args.length > 1) {

			//TODO: Fix stuff

//			try {
//				RequestBuffer.request(() -> {
//					String ending = args[1].replaceAll(".*\\.(.+?)$", "\\.$1");
//					System.out.println(ending);
//					bot.getClient().changeAvatar(Image.forUrl(ending, args[1]));
//				}).get();
//				RequestBuffer.request(() -> {
//					message.getChannel().sendMessage("Successfully changed the avatar of the bot to " + bot.getClient().getOurUser().getAvatarURL());
//				});
//			} catch (DiscordException e) {
//				RequestBuffer.request(() -> {
//					message.getChannel().sendMessage("Failed to change the avatar of the bot.");
//				});
//			}
		}
	}

	@Override
	public void onLoad() {

	}
}
