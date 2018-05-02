package me.bot.commands.superadmin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.configs.HTTP;
import me.main.PermissionManager;
import me.main.Prefixes;

import java.io.File;
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
		return new String[]{"test"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return new String[]{Prefixes.getSuperAdminPrefix()};
	}

	@Override
	public boolean hasPermissions(User user, Guild guild) {
		return PermissionManager.isBotAdmin(user);
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
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
				File file = new File("./resources/configs/" + message.getGuild() + "/" + author.getId().asLong(),filename);
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
