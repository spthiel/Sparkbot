package me.console.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import me.bot.base.Bot;
import me.console.ConsoleCommand;
import me.main.Main;
import me.main.PermissionManager;

import java.awt.*;
import java.util.ArrayList;

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


			switch(args[1]) {
				case "get":
					logGet(Main.getBot());
					break;
			}

		} else {


			String action = args[1];

			switch (action) {
				case "get":
					logGet(Main.getBot());
					break;
				case "add":
					String idstring = args[2];

					if (!idstring.matches("\\d+")) {
						return;
					}

					long id = Long.parseLong(idstring);
					if(PermissionManager.isBotAdmin(id)) {
						return;
					}

					boolean returned = PermissionManager.addBotAdmin(id);

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
		String[] out = {"team","botteam"};
		return out;
	}

	public void logGet(Bot bot) {
		ArrayList<User> owner = new ArrayList<>();
		ArrayList<User> admins = new ArrayList<>();
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

		admins.forEach(user -> adminsBuilder.append(user.getUsername()).append("\n"));
		owner.forEach(user -> ownerBuilder.append(user.getUsername()).append("\n"));


		System.out.println("Owners:\n" + ownerBuilder.toString() + "\nAdmins:\n" + adminsBuilder.toString());

	}
}