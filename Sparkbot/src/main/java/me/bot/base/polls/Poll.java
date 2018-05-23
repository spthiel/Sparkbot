package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import me.bot.base.Bot;
import me.main.Triplet;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Poll<T> {

	private Bot bot;
	private User user;
	private MessageChannel channel;
	private boolean skipable;
	private long timeUntilInactive;
	private boolean ended;
	private long lastInteraction;

	private ArrayList<BiConsumer<T,PollExitType>> simpleSubscribers;

	public Poll(Bot bot, User user, MessageChannel channel, boolean skipable, long timeUntilInactive) {
		this.bot = bot;
		this.user = user;
		this.channel = channel;
		this.skipable = skipable;
		this.timeUntilInactive = timeUntilInactive;
		ended = false;

		simpleSubscribers = new ArrayList<>();
	}

	abstract public boolean onTrigger(Message message);
	abstract public void sendMessage();
	abstract public void deleteLastMessage();

	public void registerInteraction() {
		this.lastInteraction = System.currentTimeMillis();
	}

	public long getLastInteraction() {
		return lastInteraction;
	}

	public long getTimeUntilInactive() {
		return timeUntilInactive;
	}

	public Bot getBot() {
		return bot;
	}

	public User getUser() {
		return user;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void onEnd(final T result) {
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(result,PollExitType.SUCCESS));
		ended = true;
	}

	public void onSkip() {
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null,PollExitType.SKIP));
		ended = true;
	}

	public void onExit() {
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null,PollExitType.EXIT));
		ended = true;

	}

	public void onInactiv() {

		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null,PollExitType.INACTIVE));
		ended = true;
	}

	public boolean isSkipable() {
		return skipable;
	}

	public long getUserID() {
		return user.getId().asLong();
	}

	public boolean ended() {
		return ended;
	}

	public void subscribe(BiConsumer<T,PollExitType> subscriber) {
		simpleSubscribers.add(subscriber);
	}

}
