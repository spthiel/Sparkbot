package me.bot.commands.admin;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Entry;
import me.main.Prefixes;

import discord4j.rest.util.Color;
import discord4j.rest.util.Image;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
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
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
		if(args.length < 1) {
			return;
		}

		if(args.length == 1) {


			switch(args[0]) {
				case "get":
					logGet(bot,channel,guild);
					break;
				default:
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
					if(!bot.getPermissionManager().isBotOwner(author)) {
						return;
					}
					long id = Long.parseLong(idstring);
					if(bot.getPermissionManager().isBotAdmin(id)) {
						return;
					}

					boolean returned = bot.getPermissionManager().addBotAdmin(id);

					String out = (returned ? "Successfully added <@" + id + "> to bot admins." : "Failed to add <@" + id + "> to bot admins.");

					channel.createMessage(spec -> spec.setContent(out)).subscribe();

					break;
				case "remove":
					idstring = args[1];
					if (!idstring.matches("\\d+")) {
						return;
					}
					if(!bot.getPermissionManager().isBotOwner(author)) {
						return;
					}
					id = Long.parseLong(idstring);
					if(bot.getPermissionManager().isBotOwner(id)) {
						return;
					}

					returned = bot.getPermissionManager().removeBotAdmin(id);

					out = (returned ? "Successfully removed <@" + id + "> to bot admins." : "Failed to remove <@" + id + "> to bot admins.");

					channel.createMessage(spec -> spec.setContent(out)).subscribe();

					break;
			}
		}

	}

	private static String AVATAR_URL = "https://cdn.discordapp.com/avatars/%ID%/%HASH%.png?size=2048";

	public void logGet(Bot bot, MessageChannel channel, Guild guild) {

		Entry<List<Snowflake>,List<Snowflake>> entry = bot.getPermissionManager().getBotAdmins();

		Flux.fromIterable(entry.getKey())
			.flatMap(bot.getGateway()::getUserById)
			.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
			.collectList()
			.zipWith(Flux.fromIterable(entry.getValue())
				.flatMap(bot.getGateway()::getUserById)
				.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
				.collectList()).subscribe(
					result -> {
						List<User> owners = result.getT1();
						List<User> admins = result.getT2();

						StringBuilder adminsBuilder = new StringBuilder();
						StringBuilder ownerBuilder = new StringBuilder();

						if(admins.size() != 0)
							admins.forEach(user -> adminsBuilder.append("<@").append(user.getId().asLong()).append(">\n"));
						else
							adminsBuilder.append("-- None --");

						if(owners.size() != 0)
							owners.forEach(user -> ownerBuilder.append("<@").append(user.getId().asLong()).append(">\n"));
						else
							ownerBuilder.append("-- None --");

						bot.getGateway().getSelf().subscribe(user -> {
							String url = user.getAvatarUrl(Image.Format.PNG).orElse("");
							
							Consumer<EmbedCreateSpec> embed = (e) -> e
									.setColor(Color.of(0x890083))
									.setThumbnail(url)
									.setAuthor("Sparkbot Team","","")
									.addField("Owners", ownerBuilder.toString(), true)
									.addField("Admins", adminsBuilder.toString(), true);
							
							channel.createMessage(spec -> spec.setEmbed(embed)).subscribe();
						});

					}
		);

	}

	@Override
	public void onLoad(final Bot bot) {

		System.out.println("on load called");
		bot.getPermissionManager().setupPermfile();
		if(!bot.getPermissionManager().isBotOwner(261538420952662016L)) {
			System.out.println("Add owner required");
			bot.getPermissionManager().addBotOwner(261538420952662016L);
		}

	}

}
