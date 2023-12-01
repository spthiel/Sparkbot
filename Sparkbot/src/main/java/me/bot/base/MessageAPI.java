package me.bot.base;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SuppressWarnings("unused")
public class MessageAPI {
	
	public static void sendAndDeleteMessageLater(final MessageChannel channel, final String content, long lifetime) {
		
		deleteLater(channel.createMessage(MessageCreateSpec.builder()
														   .content(content)
														   .build()), lifetime);
	}
	
	public static void sendAndDeleteMessageLater(final MessageBuilder content, long lifetime) {
		
		deleteLater(content.send(), lifetime);
	}
	
	private static void deleteLater(Mono<Message> messageMono, long lifetime) {
		
		messageMono
			.delayElement(Duration.ofMillis(lifetime))
			.flatMap(Message :: delete)
			.subscribe();
	}
	
}
