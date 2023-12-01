package me.console.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Flux;

import me.bot.base.Bot;
import me.console.ConsoleCommand;
import me.main.Entry;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Team implements ConsoleCommand {

	@Override
	public String getHelp() {
		return "Modifies Team.";
	}

	@Override
	public void run(String... args) {
		
		System.out.println("Team " + Arrays.toString(args));

		if(args.length == 2) {
			
			//noinspection SwitchStatementWithTooFewBranches
			switch(args[1]) {
				case "get":
					logGet(Objects.requireNonNull(Bot.getBotByName(args[0])));
					break;
				default:
			}

		} else {

			Bot bot = Bot.getBotByName(args[0]);
			String action = args[1];
			String idstring;
			long id;
			boolean returned;
			String out;

			switch (action) {
				case "get":
					logGet(Objects.requireNonNull(Bot.getBotByName(args[0])));
					break;
				case "add":
					idstring = args[2];

					if (!idstring.matches("\\d+")) {
						System.out.println("id is not a number");
						return;
					}

					id = Long.parseLong(idstring);
					if(Objects.requireNonNull(bot).getPermissionManager().isBotAdmin(id)) {
						System.out.println("User is already moderation");
						return;
					}

					returned = bot.getPermissionManager().addBotOwner(id);

					out = (returned ? "Successfully added " + id + " to bot admins." : "Failed to add " + id + " to bot admins.");

					System.out.println(out);
					break;
				case "remove":
					bot = Bot.getBotByName(args[0]);
					idstring = args[2];
					if (!idstring.matches("\\d+")) {
						return;
					}
					id = Long.parseLong(idstring);
					if(Objects.requireNonNull(bot).getPermissionManager().isBotOwner(id)) {
						return;
					}

					returned = bot.getPermissionManager().removeBotAdmin(id);

					out = (returned ? "Successfully removed " + id + " to bot admins." : "Failed to remove " + id + " to bot admins.");

					System.out.println(out);

					break;
			}
		}

	}

	@Override
	public void onLoad() {

	}

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public String[] getNames() {
		return new String[]{"team","botteam"};
	}

	public void logGet(Bot bot) {

		Entry<java.util.List<Snowflake>,java.util.List<Snowflake>> entry = bot.getPermissionManager().getBotAdmins();

		Flux.fromIterable(entry.getKey())
				.flatMap(bot.getGateway()::getUserById)
				.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
				.collectList()
				.zipWith(Flux.fromIterable(entry.getValue())
							 .flatMap(bot.getGateway()::getUserById)
							 .sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
							 .collectList()).subscribe(
				result -> {
					java.util.List<User> owners = result.getT1();
					List<User> admins = result.getT2();

					StringBuilder adminsBuilder = new StringBuilder();
					StringBuilder ownerBuilder = new StringBuilder();

					admins.forEach(user -> adminsBuilder.append(user.getUsername()).append("\n"));
					owners.forEach(user -> ownerBuilder.append(user.getUsername()).append("\n"));


					System.out.println("Owners:\n" + ownerBuilder + "\nAdmins:\n" + adminsBuilder);
				}

		);




	}
}