package me.bot.base.polls;


import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import me.bot.base.Bot;
import me.bot.base.MessageBuilder;

public class Input extends Poll<String> {

	private String question;
	private String tail;

	private Message lastMessage;

	public Input(Bot bot, User user, MessageChannel channel, String question, String tail, boolean skipable, long inactiveTime) {
		super(bot,user,channel,skipable,inactiveTime);
		this.question = question.replace("```\\w+|`","");
		this.tail = tail;
	}

	@Override
	public boolean onTrigger(Message message) {
		onEnd(message.getContent());
		deleteLastMessage();
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
			lastMessage.delete().subscribe();
		}
	}
}
