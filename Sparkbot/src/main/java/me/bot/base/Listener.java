package me.bot.base;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Permission;
import me.bot.base.polls.Poll;
import me.main.PermissionManager;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Listener {

	public ArrayList<ICommand> commands = new ArrayList<>();
	private List<Poll> polls = new ArrayList<>();
	private Bot bot;

	public Listener(Bot bot) {
		this.bot = bot;
		Thread clearPolls = new Thread(() -> {
			//noinspection InfiniteLoopStatement
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
				} catch (InterruptedException ignored) {
				}
			}
		});

		clearPolls.start();

		Reflections reflections = new Reflections("me.bot.commands");
		reflections.getSubTypesOf(ICommand.class).forEach(i -> {
			try {
				ICommand command = i.newInstance();
				addCommands(command);
			} catch (Exception ex) {
			}

		});
	}

	public List<ICommand> getCommands() {
		return commands;
	}

	public void addCommands(ICommand... c) {
		Arrays.stream(c).forEach(ICommand -> {
			ICommand.onLoad();
			commands.add(ICommand);
			System.out.println(ICommand.getNames()[0] + " has been enabled.");
		});
	}

	public void addPoll(Poll poll) {
		polls.add(poll);
		poll.sendMessage();
	}

	public void onDelete(MessageDeleteEvent e) {
//		if (e.getAuthor().isBot()) {
//    		long guild = e.getGuild().getLongID();
//    		//bot.getResourceManager().isSet("configs")
//			//if()
//	    }
	}

	public void onReadyEvent(ReadyEvent event) {

		String name = bot.getName();

//		if (!bot.getClient().getOurUser().getName().equalsIgnoreCase(name))
//			bot.getClient().changeUsername(name);

		updatePresence();

	}

	public void onJoinServer(GuildCreateEvent event) {
		updatePresence();
	}

	private void updatePresence() {
		int servercount = 0;
		for(Guild g : bot.getClient().getGuilds().toIterable()) {
			servercount++;
		}

		String message = servercount + " Server" + (servercount > 1 ? "s" : "");
		if(!bot.isStreaming()) {
			bot.getClient().updatePresence(Presence.online(Activity.playing(message))).block();
			System.out.println("Changed playing presence");
		} else {
			bot.getClient().updatePresence(Presence.online(Activity.streaming(message,bot.getUrl()))).block();
			System.out.println("Changed streaming presence");
		}
	}

	public void onMessageReceivedEvent(MessageCreateEvent event) {

		User user = event.getMessage().getAuthor().block();
		if (user == null || user.isBot()) {
			return;
		}

		Poll poll = getPollOfPlayer(user.getId().asLong());

		if (poll != null) {

			MessageChannel channel = event.getMessage().getChannel().block();

			String messagecontent = event.getMessage().getContent().orElse("");

			if (messagecontent.startsWith("skip")) {

				if (!poll.skipAble()) {
					sendNotAValidPollRespond(channel);
				} else {
					poll.onSkip();
				}

			} else if (messagecontent.startsWith("exit")) {
				poll.onExit();
			} else {
				if(!poll.onTrigger(event.getMessage()))
					sendNotAValidPollRespond(channel);
			}

		} else {

			String message = event.getMessage().getContent().orElse("");

			Guild guild = event.getMessage().getGuild().block();
			MessageChannel channel = event.getMessage().getChannel().block();
			if(channel == null)
				return;

			for (ICommand command : commands) {

				for (String prefix : command.getPrefixes(guild)) {

					if (message.startsWith(prefix)) {

						String messageWithoutPrefix = message.substring(prefix.length(), message.length());
						String[] args = messageWithoutPrefix.split(" ");

						if (args.length == 0)
							continue;

						for (String name : command.getNames()) {


							if (args[0].equalsIgnoreCase(name) && hasPermission(command, guild, user)) {


								if (command.requiredBotPermissions() != null) {
									List<Permission> required = requiredPermissions(guild, command.requiredBotPermissions());
									if (required != null && required.size() != 0) {
										MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| I need `" + permsToString(required) + "` permissions to perfom that command.**", 5000L);
										return;
									}
								}

								System.out.println(user.getUsername() + " issued " + message);
								command.run(bot, user, channel, guild, message, event.getMessage(), args);
								return;
							} else if (args[0].equalsIgnoreCase(name)) {
								System.out.println(user.getUsername() + " failed to issue " + message);
								MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> | **" + user.getUsername() + "** You don't have enough permissions to perform that command.", 5000L);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void sendNotAValidPollRespond(MessageChannel channel) {

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

	private boolean hasPermission(ICommand command, Guild guild, User author) {
		if (PermissionManager.isBotAdmin(author))
			return true;
		else
			return command.hasPermissions(author, guild);

	}

	//TODO: Add permission check
	private List<Permission> requiredPermissions(Guild guild, List<Permission> perms) {
		return null;
//		EnumSet<Permission> set = bot.getBotuser();
//		if (set.contains(Permission.ADMINISTRATOR))
//			return null;
//		else {
//			List<Permission> out = null;
//			for (Permission perm : perms) {
//				if (!set.contains(perm))
//					if (out == null) {
//						out = new ArrayList<>();
//						out.add(perm);
//					} else
//						out.add(perm);
//
//			}
//			return out;
//		}
	}

	private String permsToString(List<Permission> perms) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < perms.size(); i++) {
			Permission p = perms.get(i);
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
