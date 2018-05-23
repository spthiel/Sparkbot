package me.bot.commands.superadmin;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.PermissionManager;
import me.main.Prefixes;

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
		return new String[]{"team","botteam"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return new String[]{Prefixes.getSuperAdminPrefix()};
	}

	@Override
	public boolean hasPermissions(User user, Guild guild) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		if(args.length < 1) {
			return;
		}

		if(args.length == 1) {


			switch(args[0]) {
				case "get":
					logGet(bot,channel,guild);
					break;
			}

		} else {


			String action = args[0];

			switch (action) {
				case "get":
					logGet(bot,channel,guild);
					break;
				case "add":
					String idstring = args[1];

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

					channel.createMessage(new MessageCreateSpec().setContent(out));

					break;
				case "remove":
					idstring = args[1];
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

					channel.createMessage(new MessageCreateSpec().setContent(out));

					break;
			}
		}

	}

	public void logGet(Bot bot, MessageChannel channel, Guild guild) {
		List<User> owner = new ArrayList<>();
		List<User> admins = new ArrayList<>();
		PermissionManager.getBotAdmins().forEach(m -> {
			Snowflake id = Snowflake.of(m);
			if(PermissionManager.isBotOwner(m)) {
				owner.add(bot.getClient().getUserById(id).block());
			} else {
				admins.add(bot.getClient().getUserById(id).block());
			}
		});

		admins.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()));
		owner.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()));

		StringBuilder adminsBuilder = new StringBuilder();
		StringBuilder ownerBuilder = new StringBuilder();

		if(admins.size() != 0)
			admins.forEach(user -> adminsBuilder.append("<@").append(user.getId().asLong()).append(">\n"));
		else
			adminsBuilder.append("-- None --");

		if(owner.size() != 0)
			owner.forEach(user -> ownerBuilder.append("<@").append(user.getId().asLong()).append(">\n"));
		else
			ownerBuilder.append("-- None --");



		EmbedCreateSpec embed = new EmbedCreateSpec()
				.setColor(new Color(890083).getRGB())
				.setThumbnail(bot.getBotuser().getAvatarHash().orElse(""))
				.setAuthor("Sparkbot Team","","")
				.addField("Owners", ownerBuilder.toString(), true)
				.addField("Admins", adminsBuilder.toString(), true);

		channel.createMessage(new MessageCreateSpec().setEmbed(embed));
	}

	@Override
	public void onLoad() {
		PermissionManager.setupPermfile();
		if(!PermissionManager.isBotOwner(261538420952662016L)) {
			PermissionManager.addBotOwner(261538420952662016L);
		}

	}

}
