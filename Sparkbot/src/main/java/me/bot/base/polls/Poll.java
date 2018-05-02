package me.bot.base.polls;

import discord4j.core.object.entity.Message;

public interface Poll<T> {

	boolean onTrigger(Message message);
	void sendMessage();
	void onExit();
	long getUserID();
	boolean skipAble();
	void onSkip();
	long startTime();
	void onStop();
	long getTimeUntilInactivity();
	boolean ended();
	T get();

}
