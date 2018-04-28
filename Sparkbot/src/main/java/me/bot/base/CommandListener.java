package me.bot.base;

import me.bot.base.polls.Poll;
import me.main.PermissionManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unused")
public class CommandListener {

	public ArrayList<ICommand> commands = new ArrayList<>();
	private List<Poll> polls = new ArrayList<>();
	private Bot bot;

	public CommandListener(Bot bot) {
		this.bot = bot;
		Thread clearPolls = new Thread(() -> {
			while(true) {
				for (int i = 0; i < polls.size(); i++) {
					Poll poll = polls.get(i);
					if (poll.getTimeUntilInactivity() != -1 && poll.getTimeUntilInactivity() < System.currentTimeMillis() - poll.startTime()) {
						polls.remove(i).onStop();
					} else if(poll.ended())
						polls.remove(i);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		});

		clearPolls.start();
	}

	public List<ICommand> getCommands() {
		return commands;
	}

	public void addCommands(ICommand... c) {
		Arrays.stream(c).forEach(command -> {
			command.onLoad();
			commands.add(command);
			System.out.println(command.getNames()[0] + " has been enabled.");
		});
	}

	public void addPoll(Poll poll) {
		polls.add(poll);
		poll.sendMessage();
	}

	@EventSubscriber
	public void onDelete(MessageDeleteEvent e) {
		/*if (e.getAuthor().isBot()) {
    		long guild = e.getGuild().getLongID();
    		//bot.getResourceManager().isSet("configs")
			//if()
	    }*/
	}

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {

		String name = bot.getName();

		if (!bot.getClient().getOurUser().getName().equalsIgnoreCase(name))
			bot.getClient().changeUsername(name);

		updatePresence();

	}

	@EventSubscriber
	public void onJoinServer(GuildCreateEvent event) {
		updatePresence();
	}

	private void updatePresence() {
		int servers = bot.getClient().getGuilds().size();
		String message = servers + " Server" + (servers > 1 ? "s" : "");
		if(!bot.isStreaming()) {

		} else {
			bot.getClient().changeStreamingPresence(StatusType.ONLINE,message,bot.getUrl());
		}
	}

	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {


		if (event.getAuthor().isBot()) {
			return;
		}

		Poll poll = getPollOfPlayer(event.getAuthor().getLongID());

		if (poll != null) {

			String messagecontent = event.getMessage().getContent();

			if (messagecontent.startsWith("skip")) {

				if (!poll.skipAble()) {
					sendNotAValidPollRespond(event.getChannel());
				} else {
					poll.onSkip();
				}

			} else if (messagecontent.startsWith("exit")) {
				poll.onExit();
			} else {
				if(!poll.onTrigger(event.getMessage()))
					sendNotAValidPollRespond(event.getChannel());
			}

		} else {

			String message = event.getMessage().getContent();

			for (ICommand command : commands) {

				for (String prefix : command.getPrefixes(event.getMessage().getGuild())) {

					if (message.startsWith(prefix)) {

						String messageWithoutPrefix = message.substring(prefix.length(), message.length());
						String[] args = messageWithoutPrefix.split(" ");

						if (args.length == 0)
							continue;

						for (String name : command.getNames()) {


							if (args[0].equalsIgnoreCase(name) && hasPermission(command, event.getGuild(), event.getAuthor())) {


								if (command.requiredBotPermissions() != null) {
									List<Permissions> required = requiredPermissions(event.getGuild(), command.requiredBotPermissions());
									if (required != null && required.size() != 0) {
										MessageAPI.sendAndDeleteMessageLater(event.getChannel(), "<:red_cross:398120014974287873> **| I need `" + permsToString(required) + "` permissions to perfom that command.**", 5000L);
										return;
									}
								}

								bot.LOGGER.info(event.getAuthor().getName() + " issued " + event.getMessage().getContent());
								command.run(bot, event.getAuthor(), event.getMessage(), args);
								return;
							} else if (args[0].equalsIgnoreCase(name)) {
								bot.LOGGER.info(event.getAuthor().getName() + " failed to issue " + event.getMessage().getContent());
								MessageAPI.sendAndDeleteMessageLater(event.getChannel(), "<:red_cross:398120014974287873> **| <@" + event.getAuthor().getLongID() + "> You don't have enough permissions to perform that command", 5000L);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void sendNotAValidPollRespond(IChannel channel) {

	}

	private Poll getPollOfPlayer(long userid) {
		long now = System.currentTimeMillis();
		for (int i = 0; i < polls.size(); i++) {
			Poll poll = polls.get(i);
			if(poll.ended())
				polls.remove(i);
			else if (now - poll.startTime() > poll.getTimeUntilInactivity() && poll.getTimeUntilInactivity() >= 0)
				polls.remove(i).onStop();
			else if (poll.getUserID() == userid)
				return poll;
		}
		return null;

	}

	private boolean hasPermission(ICommand command, IGuild guild, IUser author) {
		if (PermissionManager.isBotAdmin(author))
			return true;
		else
			return command.hasPermissions(guild, author);
	}

	private List<Permissions> requiredPermissions(IGuild guild, List<Permissions> perms) {
		EnumSet<Permissions> set = bot.getClient().getOurUser().getPermissionsForGuild(guild);
		if (set.contains(Permissions.ADMINISTRATOR))
			return null;
		else {
			List<Permissions> out = null;
			for (Permissions perm : perms) {
				if (!set.contains(perm))
					if (out == null) {
						out = new ArrayList<>();
						out.add(perm);
					} else
						out.add(perm);

			}
			return out;
		}
	}

	private String permsToString(List<Permissions> perms) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < perms.size(); i++) {
			Permissions p = perms.get(i);
			if (perms.size() == 1) {
				out
					.append("`")
					.append(p.toString())
					.append("`");
			} else if (i != perms.size() - 1) {
				out
					.append("`")
					.append(p.toString())
					.append("`, ");
			} else {
				out
					.append("and `")
					.append(p.toString())
					.append("`");
			}
		}
		return out.toString();
	}
}
