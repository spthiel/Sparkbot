package me.bot.base.polls;


import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import me.bot.base.Bot;
import me.bot.base.MessageBuilder;

public class Input implements Poll<String> {

	private Bot bot;
	private User user;
	private MessageChannel channel;
	private String leaveMessage;
	private String question;
	private String tail;
	private boolean skipable;

	private Message lastMessage;

	private long inactiveTime;
	private long lastStart;

	private boolean end = false;

	private String ret;

	public Input(Bot bot, User user, MessageChannel channel,String question, String tail, String leaveMessage, boolean skipable, long inactiveTime) {
		this.bot = bot;
		this.user = user;
		this.channel = channel;
		this.leaveMessage = leaveMessage;
		this.skipable = skipable;
		this.question = question.replace("```\\w+|`","");
		this.tail = tail;
		this.inactiveTime = inactiveTime;
		lastStart = System.currentTimeMillis();
	}

	@Override
	public boolean onTrigger(Message message) {
		ret = message.getContent().orElse("");
		removeLastMessage();
		return true;
	}

	@Override
	public void sendMessage() {
		removeLastMessage();
		lastStart = System.currentTimeMillis();

		MessageBuilder builder = new MessageBuilder(bot.getClient());

		builder.withChannel(channel);

		builder.appendContent("```\n")
				.appendContent(question)
				.appendContent("\n```\n")
				.appendContent(tail)
				;

		builder.send().subscribe(
				message -> lastMessage = message
		);
	}

	@Override
	public void onExit() {
		removeLastMessage();
		end = true;
	}

	@Override
	public long getUserID() {
		return user.getId().asLong();
	}


	@Override
	public boolean skipAble() {
		return skipable;
	}

	@Override
	public void onSkip() {
		removeLastMessage();
		ret = "skip";
		end = true;
	}

	@Override
	public long startTime() {
		return lastStart;
	}

	@Override
	public void onStop() {
		removeLastMessage();
		ret = "stop";
		end = true;
	}

	@Override
	public long getTimeUntilInactivity() {
		return inactiveTime;
	}

	@Override
	public boolean ended() {
		return end || ret != null;
	}

	@Override
	public String get() {
		while(!ended()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		return ret;
	}

	private void removeLastMessage() {
		if(lastMessage != null) {
			lastMessage.delete();
		}
	}
}
