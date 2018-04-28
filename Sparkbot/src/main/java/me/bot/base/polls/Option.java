package me.bot.base.polls;

import me.bot.base.Bot;
import me.bot.base.MessageAPI;
import me.main.Entry;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Option implements Poll<Integer>{

	private IUser user;
	private IChannel channel;
	private Bot bot;

	private ArrayList<String> executions;
	private String leaveMessage;
	private boolean skipable;
	private IMessage lastMessage;
	private String
			head,
			tail;
	private int page;
	private long start = 0;
	private long timeUntilInactive;
	private int ret = -1;

	private boolean end = false;

	public Option (Bot bot, IUser user, IChannel channel, String head, String tail, String leaveMessage, boolean skipable, long timeUntilInactive) {
		this.head = head.replace("```\\w+|`","");
		this.tail = tail;
		this.leaveMessage = leaveMessage;
		this.user = user;
		this.page = 0;
		this.channel = channel;
		this.bot = bot;
		this.timeUntilInactive = timeUntilInactive;
		this.skipable = skipable;

		executions = new ArrayList<>();
	}

	@Override
	public boolean onTrigger(IMessage message) {

		String input = message.getContent().trim().replaceAll("```\\w*|`|\\*|~|_","");

		if(!input.matches("\\d+"))
			return false;

		int option = Integer.parseInt(input);

		if(option <= 0)
			return false;

		int pagesize = pageSize();

		if(option > pagesize + (hasNext() ? 1 : 0) + (hasPrev() ? 1 : 0)) {
			return false;
		}


		if(option == pagesize + 1) {
			if(hasPrev())
				prevPage();
			else if(hasNext())
				nextPage();
		} else if(option == pagesize + 2 && hasNext() && hasPrev()) {
			nextPage();
		} else {

			int absoluteoption = page * 7 + option - 1;
			ret = absoluteoption;

			MessageAPI.deleteMessage(lastMessage);
		}


		return true;
	}

	@Override
	public void sendMessage() {

		if(lastMessage != null && !lastMessage.isDeleted())
			MessageAPI.deleteMessage(lastMessage);

		start = System.currentTimeMillis();

		MessageBuilder builder = new MessageBuilder(bot.getClient())
				.withChannel(channel)
				.appendContent("```")
				.appendContent(head)
				.appendContent("\n");
		int pagestart = page*7;
		int pagesize = pageSize();
		int pageend = pagestart+pagesize;
		int maxpage = executions.size()/7+1;

		builder.appendContent("\nPage: " + (page+1) + "\n");

		for(int i = pagestart; i < pageend; i++) {
			builder.appendContent("- [" + (i-pagestart+1) + "] " + executions.get(i) + "\n");
		}


		if(hasPrev())
			builder.appendContent("- [" + (pagesize + 1) + "] Previous page\n");

		if(hasNext() && !hasPrev())
			builder.appendContent("- [" + (pagesize + 1) + "] Next page\n");
		else if(hasNext())
			builder.appendContent("- [" + (pagesize + 2) + "] Next page\n");

		builder.appendContent("```\n\n");
		builder.appendContent(tail);

		lastMessage = MessageAPI.sendMessage(builder);
	}

	@Override
	public void onExit() {
		if(lastMessage != null && !lastMessage.isDeleted())
			MessageAPI.deleteMessage(lastMessage);
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
		ret = -2;
		end = true;
	}

	@Override
	public long startTime() {
		return start;
	}

	@Override
	public void onStop() {
		if(lastMessage != null && !lastMessage.isDeleted())
			MessageAPI.deleteMessage(lastMessage);
		end = true;
	}

	@Override
	public long getTimeUntilInactivity() {
		return timeUntilInactive;
	}

	@Override
	public boolean ended() {
		return end || ret != -1;
	}

	@Override
	public Integer get() {

		while(!ended()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		return ret;

	}

	public void appendOption(String option) {
		executions.add(option);
	}

	public void appendOptions(List<String> options) {
		executions.addAll(options);
	}

	public void appendOptions(String... options) {
		executions.addAll(Arrays.asList(options));
	}

	private int pageSize() {
		return (page+1)*7 < executions.size() ? 7 : executions.size() - page*7;
	}

	private void nextPage() {
		page++;
		sendMessage();
	}

	private void prevPage() {
		page--;
		sendMessage();
	}

	private boolean hasNext() {
		return (page+1)*7 < executions.size();
	}

	private boolean hasPrev() {
		return page != 0;
	}

}
