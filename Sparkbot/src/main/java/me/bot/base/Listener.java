package me.bot.base;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.*;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import me.bot.base.polls.Poll;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unused", "WeakerAccess"})
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
					if (poll.getTimeUntilInactive() > -1 && poll.getTimeUntilInactive() < System.currentTimeMillis() - poll.getLastInteraction()) {
						polls.remove(i).onInactiv();
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

	}

	public List<ICommand> getCommands() {
		return commands;
	}

	public void addCommands(Bot bot, ICommand... c) {
		Arrays.stream(c).forEach(command -> {
			command.onLoad(bot);
			commands.add(command);
			System.out.println(command.getNames()[0] + " has been enabled.");
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

		updatePresence();

	}

	public void onJoinServer(GuildCreateEvent event) {
		updatePresence();
	}

	private void updatePresence() {

		bot.getClient().getGuilds().count().doOnSuccess(
			count -> {
				String message = count + " Server" + (count > 1 ? "s" : "") + " | s!help";
				if(!bot.isStreaming()) {
					bot.getClient().updatePresence(Presence.online(Activity.playing(message))).subscribe(
							aVoid -> System.out.println("Changed playing presence")
					);
				} else {
					bot.getClient().updatePresence(Presence.online(Activity.streaming(message,bot.getUrl()))).subscribe(
							aVoid -> System.out.println("Changed streaming presence")
					);
				}
			}
		);

	}

	public void onMessageReceivedEvent(final MessageCreateEvent event) {

		Snowflake guildidsf = event.getGuildId().orElse(null);
		if (guildidsf != null) {
			long guildid = guildidsf.asLong();
			event.getMessage().getChannel()
				.filter(messageChannel -> messageChannel instanceof TextChannel)
				.filter(messageChannel -> event.getMember().isPresent() && !event.getMember().get().isBot())
				.zipWith(event.getMessage().getGuild())
					.subscribe(
						objects -> {
							Member member = event.getMember().get();

							TextChannel channel = (TextChannel)objects.getT1();
							Guild guild = objects.getT2();

							final Poll poll = getPollOfPlayer(member.getId().asLong());
							if (poll != null) {

								final String messagecontent = event.getMessage().getContent().orElse("");

								if (messagecontent.startsWith("skip")) {

									if (!poll.isSkipable()) {
										sendNotAValidPollRespond(channel);
									} else {
										poll.onSkip();
									}

								} else if (messagecontent.startsWith("exit")) {
									poll.onExit();
								} else {
									if (!poll.onTrigger(event.getMessage()))
										sendNotAValidPollRespond(channel);
								}

							} else {
								procressCommand(member, guild, channel, event, isOnWhitelist(bot,guildid,channel));
							}
						}
					);
		}
	}

	private void procressCommand(final Member member, final Guild guild, final TextChannel channel, final MessageCreateEvent event, final boolean isOnWhitelist) {

		String message = event.getMessage().getContent().orElse("");

		if (channel == null)
			return;

		commands.forEach(command -> {

			if(isOnWhitelist || !command.getType().equals(CommandType.PUBLIC))

				for (String prefix : command.getPrefixes(guild)) {

					if (message.startsWith(prefix)) {

						String messageWithoutPrefix = message.substring(prefix.length(), message.length());
						String[] args = messageWithoutPrefix.split(" ");

						if (args.length == 0)
							continue;

						for (String name : command.getNames()) {


							if (args[0].equalsIgnoreCase(name)) {

								command.hasPermission(bot, guild, member).subscribe(
									hasPermissions -> {
										if(hasPermissions) {
											if (command.requiredBotPermissions() != null) {
												List<Permission> required = requiredPermissions(guild, command.requiredBotPermissions());
												if (required != null && required.size() != 0) {
													MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| I need `" + permsToString(required) + "` permissions to perfom that command.**", 5000L);
													return;
												}
											}

											System.out.println(member.getUsername() + " issued " + message);
											command.run(bot, member, channel, guild, event.getMessage(), args[0], Arrays.copyOfRange(args, 1, args.length), message);
										} else {
											System.out.println(member.getUsername() + " failed to issue " + message);
											MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> | **" + member.getUsername() + "** You don't have enough permissions to perform that command.", 5000L);
										}
									}
								);
							}
						}
					}
				}
		});
	}

	private boolean isOnWhitelist(Bot bot, long guildid, Channel channel) {
		List<String> whitelist = getWhitelist(bot,guildid);
		return whitelist == null || whitelist.size() == 0 || whitelist.contains(channel.getId().asString());
	}

	private List<String> getWhitelist(Bot bot, long guildid) {
		Map<String,Object> map = bot.getResourceManager().getConfig("configs/" + guildid, "whitelist.json");
		if(map.containsKey("channels")) {
			Object channels = map.get("channels");
			if(channels instanceof List && ((List) channels).size() != 0 && ((List) channels).get(0) instanceof String) {
				//noinspection unchecked
				return (List<String>)map.get("channels");
			} else
				return null;
		} else {
			return null;
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
			else if (now - poll.getLastInteraction() > poll.getTimeUntilInactive() && poll.getTimeUntilInactive() >= 0)
				polls.remove(i).onInactiv();
			else if (poll.getUserID() == userid)
				return poll;
		}
		return null;

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
