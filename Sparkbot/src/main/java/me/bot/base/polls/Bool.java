package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import me.bot.base.MessageBuilder;

public class Bool extends Poll<Boolean> {
	
	private final String question;
	private final String tail;
	
	private Message lastMessage;
	
	public Bool(User user, MessageChannel channel, String question, String tail, boolean skippable, long inactiveTime) {
		
		super(user, channel, skippable, inactiveTime);
		this.question = question.replace("```\\w+|`", "");
		this.tail = tail;
	}
	
	private static final String TRUE_REGEX  = "1|yes|true|y";
	private static final String FALSE_REGEX = "0|no|false|n";
	
	@Override
	public boolean onTrigger(Message message) {
		
		String content   = message.getContent();
		String lowercase = content.toLowerCase();
		
		if (!lowercase.matches(TRUE_REGEX) && !lowercase.matches(FALSE_REGEX)) {
			return false;
		}
		
		onEnd(lowercase.matches(TRUE_REGEX));
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
		
		builder.send()
			   .subscribe(
				   message -> lastMessage = message
			   );
	}
	
	@Override
	public void deleteLastMessage() {
		
		if (lastMessage != null) {
			lastMessage.delete()
					   .doOnError(Throwable :: printStackTrace)
					   .subscribe();
		}
		
	}
}