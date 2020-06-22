package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import me.bot.base.Bot;
import me.bot.base.MessageBuilder;

public class Bool extends Poll<Boolean> {

	private String question;
	private String tail;

	private Message lastMessage;

	public Bool(Bot bot, User user, MessageChannel channel, String question, String tail, boolean skipable, long inactiveTime) {
		super(bot,user,channel,skipable,inactiveTime);
		this.question = question.replace("```\\w+|`","");
		this.tail = tail;
	}

	private static final String TRUE_REGEX  = "1|yes|true|y";
	private static final String FALSE_REGEX = "0|no|false|n";

	@Override
	public boolean onTrigger(Message message) {

		String content = message.getContent();
		String lcase = content.toLowerCase();

		if(!lcase.matches(TRUE_REGEX) && !lcase.matches(FALSE_REGEX)) {
			return false;
		}

		onEnd(lcase.matches(TRUE_REGEX));
		return true;
	}

	@Override
	public void sendMessage() {
		deleteLastMessage();
		registerInteraction();

		MessageBuilder builder = new MessageBuilder();

		builder.withChannel(getChannel());

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
	public void deleteLastMessage() {
		if(lastMessage != null) {
			lastMessage.delete().subscribe((ignored) -> {}, Throwable::printStackTrace, () -> {});
		}

	}
}