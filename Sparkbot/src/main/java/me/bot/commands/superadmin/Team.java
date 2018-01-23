package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Team implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Modifys team";
	}

	@Override
	public String[] getNames() {
		String[] names = {"team","botteam"};
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
		if(args.length < 2) {
			return;
		}

		if(args.length == 2) {


			switch(args[1]) {
				case "get":
					logGet(bot,message.getChannel(),message.getGuild());
					break;
			}

		} else {


			String action = args[1];

			switch (action) {
				case "get":
					logGet(bot,message.getChannel(),message.getGuild());
					break;
				case "add":
					String idstring = args[2];

					if (!idstring.matches("\\d+")) {
						return;
					}
					if(!PermissionManager.isBotOwner(author)) {
						return;
					}
					long id = Long.parseLong(idstring);
					if(PermissionManager.isBotAdmin(id)) {
						return;
					}

					boolean returned = PermissionManager.addBotAdmin(id);

					String out = (returned ? "Successfully added <@" + id + "> to bot admins." : "Failed to add <@" + id + "> to bot admins.");

					RequestBuffer.request(() -> {
						message.getChannel().sendMessage(out);
					});

					break;
				case "remove":
					idstring = args[2];
					if (!idstring.matches("\\d+")) {
						return;
					}
					if(!PermissionManager.isBotOwner(author)) {
						return;
					}
					id = Long.parseLong(idstring);
					if(PermissionManager.isBotOwner(id)) {
						return;
					}

					returned = PermissionManager.removeBotAdmin(id);

					out = (returned ? "Successfully removed <@" + id + "> to bot admins." : "Failed to remove <@" + id + "> to bot admins.");

					RequestBuffer.request(() -> {
						message.getChannel().sendMessage(out);
					});

					break;
			}
		}

	}

	public void logGet(Bot bot, IChannel channel, IGuild guild) {
		List<IUser> owner = new ArrayList<>();
		List<IUser> admins = new ArrayList<>();
		PermissionManager.getBotAdmins().forEach(m -> {
			if(PermissionManager.isBotOwner(m)) {
				owner.add(bot.getUtils().getUserByID(m));
			} else {
				admins.add(bot.getUtils().getUserByID(m));
			}
		});

		admins.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
		owner.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));

		StringBuilder adminsBuilder = new StringBuilder();
		StringBuilder ownerBuilder = new StringBuilder();

		admins.forEach(user -> adminsBuilder.append("<@" + user.getLongID() + ">\n"));
		owner.forEach(user -> ownerBuilder.append("<@" + user.getLongID() + ">\n"));


		EmbedBuilder embed = new EmbedBuilder()
				.withColor(new Color(890083))
				.withThumbnail(bot.getClient().getOurUser().getAvatarURL())
				.withAuthorName("Sparkbot Team")
				.appendField("Owners", ownerBuilder.toString(), true)
				.appendField("Admins", adminsBuilder.toString(), true);

		RequestBuffer.request(() -> {
			channel.sendMessage(embed.build());
		});
	}

	@Override
	public void onLoad() {

		if(!PermissionManager.isBotOwner(261538420952662016L)) {
			PermissionManager.addBotOwner(261538420952662016L);
		}

	}

}
