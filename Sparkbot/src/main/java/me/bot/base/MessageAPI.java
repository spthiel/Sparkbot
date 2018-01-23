package me.bot.base;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

public class MessageAPI {

    public static void sendAndDeleteMessageLater(final IChannel channel ,final String s, long lifetime) {
        final IMessage message = RequestBuffer.request(() -> {
            return channel.sendMessage(s);
        }).get();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(lifetime);
                    RequestBuffer.request(() -> {
                        message.delete();
                    });
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();

    }

    public static void sendAndDeleteMessageLater(final IChannel channel, final EmbedObject embedObject, final long lifetime) {

        final IMessage message = RequestBuffer.request(() -> {
            return channel.sendMessage(embedObject);
        }).get();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(lifetime);
                    RequestBuffer.request(() -> {
                        message.delete();
                    });
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();
    }


    public static void sendAndDeleteMessageLater(final IChannel channel, final String s, final boolean b, final long lifetime) {

        final IMessage message = RequestBuffer.request(() -> {
            return channel.sendMessage(s,b);
        }).get();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(lifetime);
                    RequestBuffer.request(() -> {
                        message.delete();
                    });
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();
    }

    public static void sendAndDeleteMessageLater(final IChannel channel, final String s, final EmbedObject embedObject, final long lifetime) {

        final IMessage message = RequestBuffer.request(() -> {
            return channel.sendMessage(s,embedObject);
        }).get();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(lifetime);
                    RequestBuffer.request(() -> {
                        message.delete();
                    });
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();
    }

    public static void sendAndDeleteMessageLater(final IChannel channel, final String s, final EmbedObject embedObject, final boolean b, final long lifetime) {

        final IMessage message = RequestBuffer.request(() -> {
            return channel.sendMessage(s,embedObject,b);
        }).get();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(lifetime);
                    RequestBuffer.request(() -> {
                        message.delete();
                    });
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();
    }

    /*public static IMessage sendMessage(Bot bot, String messagekey) {

    }*/

    public static IMessage sendMessage(MessageBuilder message) {
        return RequestBuffer.request(() -> {
            return message.send();
        }).get();

    }

}
