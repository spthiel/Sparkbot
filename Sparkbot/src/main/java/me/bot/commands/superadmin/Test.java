package me.bot.commands.superadmin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.HTTP;
import me.bot.base.polls.Input;
import me.bot.base.polls.Option;
import me.main.PermissionManager;
import me.main.Prefixes;
import org.apache.commons.io.FileUtils;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Test implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Testcommand";
	}

	@Override
	public String[] getNames() {
		String[] names = {"test"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		String[] prefixes = {Prefixes.getSuperAdminPrefix()};
		return prefixes;
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		/*Option option = new Option(bot,author,message.getChannel(),"This is the head","This is the tail","This is left",5000);
		option.appendOption("First");
		option.appendOption("Second");
		option.appendOption("Third");
		option.appendOption("4");
		option.appendOption("5");
		option.appendOption("6");
		option.appendOption("7");
		option.appendOption("8");
		option.appendOption("9");
		option.appendOption("10");*/

		/*Input option = new Input(bot,author,message.getChannel(),"Question","This is the tail","Leave",false,-1);

		Thread t = new Thread(() -> {
			bot.addPoll(option);
			String ret = option.get();
			System.out.println("sth");
			System.out.println(ret);
		});

		t.start();*/

		message.getAttachments().forEach(attachment -> {
			String url = attachment.getUrl();
			String filename = attachment.getFilename();
			try {
				File file = new File("./resources/configs/" + message.getGuild() + "/" + author.getLongID(),filename);
				System.out.println(HTTP.getAsList(url));
				//FileUtils.copyURLToFile(new URL(url), file);
			} catch (Exception e) {
				System.out.println(url);
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onLoad() {

	}
}
