package me.bot.base.polls;

import me.bot.base.Bot;
import me.bot.base.MessageAPI;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

public class Bool implements Poll<Boolean> {

	private Bot bot;
	private IUser user;
	private IChannel channel;
	private String leaveMessage;
	private String question;
	private String tail;
	private boolean skipable;

	private IMessage lastMessage;

	private long inactiveTime;
	private long lastStart;

	private boolean end = false;

	private boolean ret;

	public Bool(Bot bot, IUser user, IChannel channel,String question, String tail, String leaveMessage, boolean skipable, long inactiveTime) {
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

	private final String TRUE_REGEX  = "1|yes|true|y";
	private final String FALSE_REGEX = "0|no|false|n";

	@Override
	public boolean onTrigger(IMessage message) {

		String content = message.getContent();
		String lcase = content.toLowerCase();

		if(!lcase.matches(TRUE_REGEX) && !lcase.matches(FALSE_REGEX)) {
			return false;
		}

		ret = lcase.matches(TRUE_REGEX);
		end = true;
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

		lastMessage = MessageAPI.sendMessage(builder);
	}

	@Override
	public void onExit() {
		removeLastMessage();
		end = true;
	}

	@Override
	public long getUserID() {
		return user.getLongID();
	}


	@Override
	public boolean skipAble() {
		return skipable;
	}

	@Override
	public void onSkip() {
		removeLastMessage();
		end = true;
	}

	@Override
	public long startTime() {
		return lastStart;
	}

	@Override
	public void onStop() {
		removeLastMessage();
		end = true;
	}

	@Override
	public long getTimeUntilInactivity() {
		return inactiveTime;
	}

	@Override
	public boolean ended() {
		return end;
	}

	@Override
	public Boolean get() {
		while(!ended()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		return ret;
	}

	private void removeLastMessage() {
		if(lastMessage != null && !lastMessage.isDeleted()) {
			MessageAPI.deleteMessage(lastMessage);
		}
	}
}