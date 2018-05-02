package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chars implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Design or return a char";
	}

	@Override
	public String[] getNames() {
		return new String[]{"character","char"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(User user, Guild guild) {
		return false;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
		if(args.length > 1) {
			switch(args[1]) {
				case "new":
				case "create":
					Thread t = new Thread(() -> createCharacter(bot, author, channel, guild));
					t.run();
					break;
				case "delete":
				case "remove":

					break;
				case "list":

					break;
			}
		} else {
			//TODO
		}
	}

	@Override
	public void onLoad() {

	}

	private int createCharacter(Bot bot, User user,Channel channel, Guild guild) {

		Map<String,Object> object = bot.getResourceManager().getConfig("configs/rp/" + guild.getId().asLong(), "settings.json");
		if (object.containsKey("questions")) {
			ArrayList<Object> questions = (ArrayList)object.get("questions");
			Map<String,Object> character = new HashMap<>();
			Map<String,Object> author = new HashMap<>();
			author.put("name",user.getUsername());
			author.put("id",user.getId().asLong());

			character.put("author",user);

			for (Object q : questions) {
				String question = (String)((Map<String,Object>) q).get("q");
				boolean skipable = (boolean)((Map<String,Object>) q).get("s");
				//Input input = new Input(bot, user, channel, question, "Use `exit` to leave the Menu " + (), "", false, -1);
				//bot.addPoll(input);
			}


		} else {
			return -2;
		}
		return -1;
	}
}
