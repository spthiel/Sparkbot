package me.console.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import me.bot.base.Bot;
import me.console.ConsoleCommand;
import me.main.Entry;
import me.main.Main;
import me.main.PermissionManager;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Team implements ConsoleCommand {

	@Override
	public String getHelp() {
		return "Modifies Team.";
	}

	@Override
	public void run(String... args) {
		if(args.length < 1) {
			return;
		}

		if(args.length == 1) {


			switch(args[0]) {
				case "get":
					logGet(Main.getBot());
					break;
			}

		} else {


			String action = args[0];

			switch (action) {
				case "get":
					logGet(Main.getBot());
					break;
				case "add":
					String idstring = args[1];

					if (!idstring.matches("\\d+")) {
						System.out.println("id is not a number");
						return;
					}

					long id = Long.parseLong(idstring);
					if(PermissionManager.isBotAdmin(id)) {
						System.out.println("User is already admin");
						return;
					}

					boolean returned = PermissionManager.addBotOwner(id);

					String out = (returned ? "Successfully added " + id + " to bot admins." : "Failed to add " + id + " to bot admins.");

					System.out.println(out);

					break;
				case "remove":
					idstring = args[2];
					if (!idstring.matches("\\d+")) {
						return;
					}
					id = Long.parseLong(idstring);
					if(PermissionManager.isBotOwner(id)) {
						return;
					}

					returned = PermissionManager.removeBotAdmin(id);

					out = (returned ? "Successfully removed " + id + " to bot admins." : "Failed to remove " + id + " to bot admins.");

					System.out.println(out);

					break;
			}
		}

	}

	@Override
	public void onLoad() {

	}

	@Override
	public String[] getNames() {
		return new String[]{"team","botteam"};
	}

	public void logGet(Bot bot) {

		Entry<java.util.List<Snowflake>,java.util.List<Snowflake>> entry = PermissionManager.getBotAdmins();

		Flux.fromIterable(entry.getKey())
				.flatMap(bot.getClient()::getUserById)
				.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
				.collectList()
				.zipWith(Flux.fromIterable(entry.getValue())
						.flatMap(bot.getClient()::getUserById)
						.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getUsername(),o2.getUsername()))
						.collectList()).subscribe(
				result -> {
					java.util.List<User> owners = result.getT1();
					List<User> admins = result.getT2();

					StringBuilder adminsBuilder = new StringBuilder();
					StringBuilder ownerBuilder = new StringBuilder();

					admins.forEach(user -> adminsBuilder.append(user.getUsername()).append("\n"));
					owners.forEach(user -> ownerBuilder.append(user.getUsername()).append("\n"));


					System.out.println("Owners:\n" + ownerBuilder.toString() + "\nAdmins:\n" + adminsBuilder.toString());
				}

		);




	}
}