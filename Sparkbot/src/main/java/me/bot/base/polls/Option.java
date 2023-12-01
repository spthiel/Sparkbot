package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.MessageBuilder;

@SuppressWarnings("unused")
public class Option extends Poll<Integer>{

	private final ArrayList<String> executions;
	private       Message           lastMessage;
	private final String
						 head;
	private final String tail;
	private int  page;
	@SuppressWarnings("FieldCanBeLocal")
	private long start = 0;

	public Option(Bot bot, User user, MessageChannel channel, String head, String tail, boolean skippable, long timeUntilInactive) {
		super(user, channel, skippable, timeUntilInactive);
		this.head = head.replace("```\\w+|`","");
		this.tail = tail;
		this.page = 0;

		executions = new ArrayList<>();
	}

	@Override
	public boolean onTrigger(Message message) {

		String input = message.getContent().trim().replaceAll("```\\w*|`|\\*|~|_","");

		if(!input.matches("\\d+"))
			return false;

		registerInteraction();

		int option = Integer.parseInt(input);

		if(option <= 0)
			return false;

		int pageSize = pageSize();

		if(option > pageSize + (hasNext() ? 1 : 0) + (hasPrev() ? 1 : 0)) {
			return false;
		}


		if(option == pageSize + 1) {
			if(hasPrev())
				prevPage();
			else if(hasNext())
				nextPage();
		} else if(option == pageSize + 2 && hasNext() && hasPrev()) {
			nextPage();
		} else {

			onEnd(page * 7 + option - 1);

			deleteLastMessage();
		}


		return true;
	}

	@Override
	public void sendMessage() {

		if(lastMessage != null)
			deleteLastMessage();

		start = System.currentTimeMillis();

		MessageBuilder builder = new MessageBuilder()
				.withChannel(getChannel())
				.appendContent("```")
				.appendContent(head)
				.appendContent("\n");
		int pageStart = page*7;
		int pageSize = pageSize();
		int pageEnd = pageStart+pageSize;
		int lastPage = executions.size()/7+1;

		builder.appendContent("\nPage: " + (page+1) + "\n");

		for(int i = pageStart; i < pageEnd; i++) {
			builder.appendContent("- [" + (i-pageStart+1) + "] " + executions.get(i) + "\n");
		}


		if(hasPrev())
			builder.appendContent("- [" + (pageSize + 1) + "] Previous page\n");

		if(hasNext() && !hasPrev())
			builder.appendContent("- [" + (pageSize + 1) + "] Next page\n");
		else if(hasNext())
			builder.appendContent("- [" + (pageSize + 2) + "] Next page\n");

		builder.appendContent("```\n\n");
		builder.appendContent(tail);

		builder.send().subscribe(
				message -> lastMessage = message
		);
	}

	@Override
	public void deleteLastMessage() {
		if(lastMessage != null) {
			lastMessage.delete().subscribe();
		}
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
