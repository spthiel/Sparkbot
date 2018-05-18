package me.bot.base;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
public class MessageAPI {

    public static void sendAndDeleteMessageLater(final MessageChannel channel , final String content, long lifetime) {
        deleteLater(channel.createMessage(new MessageCreateSpec().setContent(content)),lifetime);
    }

	public static void sendAndDeleteMessageLater(final MessageBuilder content, long lifetime) {
		deleteLater(content.send(),lifetime);
	}

	private static void deleteLater(Mono<Message> monomessage, long lifetime) {
    	monomessage.subscribe(
            message -> {
			    if (message == null)
				    return;

			    Thread t = new Thread(() -> {
				    try {
					    Thread.sleep(lifetime);
				    } catch (InterruptedException ignored) {
				    }
				    message.delete();
			    });

			    t.start();
		    });
	}

}
