package me.bot.commands.user;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.bot.base.polls.Input;
import me.bot.base.polls.Option;
import me.main.Prefixes;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.*;

import java.util.List;

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
		String[] names = {"character","char"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return false;
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		if(args.length > 1) {
			switch(args[1]) {
				case "new":
				case "create":
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							createCharacter(bot,author,message.getChannel());
						}
					});
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

	private int createCharacter(Bot bot, IUser user,IChannel channel) {

		JSONObject object = bot.getResourceManager().getConfig("configs/rp/" + channel.getGuild().getLongID(), "settings.json");
		if (object.has("questions")) {
			JSONArray questions = object.getJSONArray("questions");
			JSONObject character = new JSONObject();
			JSONObject author = new JSONObject();
			author.put("name",user.getName());
			author.put("id",user.getLongID());

			character.put("author",user);

			for (Object q : questions) {
				String question = ((JSONObject) q).getString("q");
				boolean skipable = ((JSONObject) q).getBoolean("s");
				//Input input = new Input(bot, user, channel, question, "Use `exit` to leave the Menu " + (), "", false, -1);
				//bot.addPoll(input);
			}


		} else {
			return -2;
		}
		return -1;
	}
}
