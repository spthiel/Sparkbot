package me.bot.commands.admin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.bot.base.polls.Bool;
import me.bot.base.polls.Input;
import me.bot.base.polls.Option;
import me.main.Prefixes;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Chars implements ICommand {
	@Override
	public String[] getNames() {
		String[] names = {"character", "char"};
		return names;
	}

	@Override
	public String getHelp() {
		return "character <add/remove/list>";
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		return Prefixes.getAdminPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		EnumSet<Permissions> perms = user.getPermissionsForGuild(guild);
		return perms.contains(Permissions.MANAGE_SERVER) || perms.contains(Permissions.ADMINISTRATOR);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		if (args.length > 1) {

			switch (args[1]) {
				case "add":
					Thread t = new Thread(() -> {
						Input poll = new Input(bot, author, message.getChannel(), "What Question would you like to add?", "Use `exit` to exit the Menu", "You left the menu", false, 30000);
						bot.addPoll(poll);
						String input = poll.get();
						if (input != null && !input.equalsIgnoreCase("stop")) {
							Bool skipable = new Bool(bot, author, message.getChannel(), "What Question would you like to add?", "Use `exit` to exit the Menu", "You left the menu", false, 30000);
							bot.addPoll(skipable);
							boolean skip = skipable.get();

							JSONObject toPut = new JSONObject();
							toPut.put("q", input);
							toPut.put("s", skip);
							JSONObject object = bot.getResourceManager().getConfig("configs/rp/" + message.getGuild().getLongID(), "settings.json");
							object.append("questions", toPut);
							bot.getResourceManager().writeConfig("configs/rp/" + message.getGuild().getLongID(), "settings.json", object);
						}
					});
					t.start();
					break;
				case "list":
					JSONObject object = bot.getResourceManager().getConfig("configs/rp/" + message.getGuild().getLongID(), "settings.json");
					if (object.has("questions")) {
						JSONArray questions = object.getJSONArray("questions");
						Option option = new Option(bot, author, message.getChannel(), "List of your questions:", "Use `exit` to leave the Menu", "", false, -1);
						for (Object q : questions) {
							String question = ((JSONObject) q).getString("q");
							option.appendOption(question);
						}
						bot.addPoll(option);


					} else {
						MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "```\nYour server has no Character questions set yet\n```", 30000);
					}
					break;
				case "remove":
					JSONObject object2 = bot.getResourceManager().getConfig("configs/rp/" + message.getGuild().getLongID(), "settings.json");
					if (object2.has("questions")) {
						JSONArray questions = object2.getJSONArray("questions");
						Option option = new Option(bot, author, message.getChannel(), "List of your questions:", "Select the question you want to delete or use `exit` to leave the Menu", "", false, -1);
						List<String> qs = new ArrayList<>();
						for (Object q : questions) {
							String question = ((JSONObject) q).getString("q");
							qs.add(question);
							option.appendOption(question);
						}
						bot.addPoll(option);
						Thread t2 = new Thread(() -> {
							qs.remove((int) option.get());
							object2.put("questions", qs);
							bot.getResourceManager().writeConfig("configs/rp/" + message.getGuild().getLongID(), "settings.json", object2);
						});

						t2.start();

					} else {
						MessageAPI.sendAndDeleteMessageLater(message.getChannel(), "```\nYour server has no Character questions set yet\n```", 30000);
					}
					break;
				default:
					MessageAPI.sendAndDeleteMessageLater(message.getChannel(), getHelp(), 10000);
			}

		} else {
			MessageAPI.sendAndDeleteMessageLater(message.getChannel(), getHelp(), 10000);
		}
	}

	@Override
	public CommandType getType() {
		return CommandType.MOD;
	}

	@Override
	public void onLoad() {
	}
}
