package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class Poll<T> {
	
	private final User           user;
	private final MessageChannel channel;
	private final boolean        skippable;
	private final long           timeUntilInactive;
	private       boolean        ended;
	private       long           lastInteraction;
	
	private final ArrayList<BiConsumer<T, PollExitType>> simpleSubscribers;
	
	public Poll(User user, MessageChannel channel, boolean skippable, long timeUntilInactive) {
		
		this.user = user;
		this.channel = channel;
		this.skippable = skippable;
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
	
	@SuppressWarnings("unused")
	public User getUser() {
		
		return user;
	}
	
	public MessageChannel getChannel() {
		
		return channel;
	}
	
	public void onEnd(final T result) {
		
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(result, PollExitType.SUCCESS));
		ended = true;
	}
	
	public void onSkip() {
		
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null, PollExitType.SKIP));
		ended = true;
	}
	
	public void onExit() {
		
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null, PollExitType.EXIT));
		ended = true;
		
	}
	
	public void onInactive() {
		
		deleteLastMessage();
		simpleSubscribers.forEach(subscriber -> subscriber.accept(null, PollExitType.INACTIVE));
		ended = true;
	}
	
	public boolean isSkippable() {
		
		return skippable;
	}
	
	public long getUserID() {
		
		return user.getId()
				   .asLong();
	}
	
	public boolean ended() {
		
		return ended;
	}
	
	public void subscribe(BiConsumer<T, PollExitType> subscriber) {
		
		simpleSubscribers.add(subscriber);
	}
	
}
