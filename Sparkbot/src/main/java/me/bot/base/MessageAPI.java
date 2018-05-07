package me.bot.base;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;

@SuppressWarnings("unused")
public class MessageAPI {

    public static void sendAndDeleteMessageLater(final MessageChannel channel , final String content, long lifetime) {
        deleteLater(channel.createMessage(new MessageCreateSpec().setContent(content)).block(),lifetime);
    }

	public static void sendAndDeleteMessageLater(final MessageBuilder content, long lifetime) {
		deleteLater(content.send(),lifetime);
	}

	private static void deleteLater(Message message,long lifetime) {
    	if(message == null)
    		return;

		Thread t = new Thread(() -> {
			try {
				Thread.sleep(lifetime);
				message.delete();
			} catch (InterruptedException ignored) {
			}
		});

		t.start();
	}

}
