package me.bot.base.polls;

import me.bot.base.Bot;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public interface Poll<T> {

	/**/

	boolean onTrigger(IMessage message);
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
