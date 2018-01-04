package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Avatar implements ICommand {
	@Override
	public String getHelp() {
		return "Changes bot's Avatar";
	}

	@Override
	public String[] getNames() {
		String[] names = {"avatar","changeavatar"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		String[] prefixes = {Prefixes.getSuperAdminPrefix()};
		return prefixes;
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		if(args.length > 1) {
			try {
				RequestBuffer.request(() -> {
					String ending = args[1].replaceAll(".*\\.(.+?)$", "\\.$1");
					System.out.println(ending);
					bot.getClient().changeAvatar(Image.forUrl(ending, args[1]));
				}).get();
				RequestBuffer.request(() -> {
					message.getChannel().sendMessage("Successfully changed the avatar of the bot to " + bot.getClient().getOurUser().getAvatarURL());
				});
			} catch (DiscordException e) {
				RequestBuffer.request(() -> {
					message.getChannel().sendMessage("Failed to change the avatar of the bot.");
				});
			}
		}
	}

	@Override
	public void onLoad() {

	}
}
