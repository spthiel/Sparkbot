package me.bot.commands.user;

import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Permission;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.IDisabledCommand;
import me.main.Prefixes;
import me.main.utils.HTTP;
import me.tablecreator.Cell;
import me.tablecreator.Grid;
import me.tablecreator.TCreator;

public class CreateTable implements ICommand, IDisabledCommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "s!table <height> <width> <content> Command to create custom tables for the botlist onDiscord.xyz";
	}

	@Override
	public String[] getNames() {
		return new String[]{"table"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	@Override
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {

		try {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("help")) {
					sendHelp(channel);
				}
				if (message.getAttachments().isEmpty()) {
					String[] code = content.replace("\n", "\\n").replaceAll("^.*?```(.+?)```.*?$", "$1").replace("\\n", "\n").split("\n");
					Grid object;
					if(code[0].trim().equalsIgnoreCase(""))
						object = TCreator.parseString(new ArrayList<>(Arrays.asList(code)),1);
					else
						object = TCreator.parseString(new ArrayList<>(Arrays.asList(code)),0);
					generateAndSendGridAsCssAndMD(object, channel);
				} else {
					getAttachment(message.getAttachments().iterator().next(), channel);
				}

			} else {
				if(!message.getAttachments().isEmpty()) {
					getAttachment(message.getAttachments().iterator().next(), channel);
				} else {
					sendHelp(channel);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			channel.createMessage(spec -> spec.setContent("<:red_cross:398120014974287873> **| Something went wrong.**")).subscribe();
		}
	}

	private void sendHelp(TextChannel channel) {
		Consumer<MessageCreateSpec> spec = s -> s.setContent("**Table creator help**\n`s!table` will run the command. You need to attach the table as .txt file or in a codeblock.");
		channel.createMessage(spec).subscribe();
	}

	@Override
	public void onLoad(final Bot bot) {

	}

	private void getAttachment(Attachment attachment,TextChannel channel) {

		if(attachment.getFilename().endsWith("txt")) {
			try {
				List<String> file = HTTP.getAsList(attachment.getUrl());
				Grid object = TCreator.parseString(file,0);
				generateAndSendGridAsCssAndMD(object,channel);

			} catch (Exception e) {
				channel.createMessage(spec -> spec.setContent("<:red_cross:398120014974287873> **| Something went wrong.**")).subscribe();
			}
		} else {
			channel.createMessage(spec -> spec.setContent("<:red_cross:398120014974287873> **| Please attach a txt file.**")).subscribe();
		}

	}

	private static final String
			TABLE_MARKDOWN = "**%ROW%**{class=\"flex-table\"}",
			ROW_MARKDOWN = "__%CELL%__{class=\"table-row%HEADER%\"}%ROW%",
			CELL_MARKDOWN = "**%CONTENT%**{class=\"table-row-item%CLASS%\"}%CELL%",
			BASECLASS = "hoverelement"
			;

	private void generateAndSendGridAsCssAndMD(Grid grid,TextChannel channel) {
		List<String> css = new ArrayList<>();
		String md = TABLE_MARKDOWN;

		css.add(".flex-table :not(.table-header) .table-row-item:not(.empty):not(:hover):after {\n    content: none;\n}\n.flex-table :not(.table-header) .table-row-item:not(.empty) {\n    cursor: help;\n}\n.flex-table :not(.table-header) .table-row-item:not(.empty):hover:after {\n    position: absolute;\n    background: rgb(30,30,30);\n    padding: 2px 10px;\n    color: white;\n    text-align: center;\n    max-width: 200px;\n    margin-top: 24px;\n    border-radius: 4px;\n    pointer-events: none;\n}\n.flex-table {\n    display: flex;\n    justify-content: space-between;\n    flex-flow: column nowrap;\n    font-weight: 100;\n    border: 1px solid black;\n    margin: 0 10px;\n    border-radius: 2px;\n}\n.table-row {\n    display: flex;\n    background-color: #f1f1f1;\n    padding: 10px;\n}\n.table-header {\n    background: rgba(144,144,144);\n    border-bottom: 1px solid black;\n}\n.table-row-item {\n    display: flex;\n    flex-flow: row nowrap;\n    flex-grow: 1;\n    flex-basis: 0;\n    justify-content: center;\n    text-align: center;\n    margin: auto;\n    font-weight: 500;\n      word-break: break-word;\n}\n.table-row-item.empty {\n    color: transparent;\n}");

		int index = grid.getStart();


		Cell[][] cells =  grid.getGrid();
		for(int y = 0; y < cells[0].length; y++) {
			if(y > 0) {
				md = md.replace("%ROW%",ROW_MARKDOWN.replace("%HEADER%",""));
			} else {
				md = md.replace("%ROW%",ROW_MARKDOWN.replace("%HEADER%"," table-header"));
			}
			
			for (Cell[] cell1 : cells) {
				
				Cell cell = cell1[y];
				if (cell == null) {
					
					md = md.replace("%CELL%", CELL_MARKDOWN
							.replace("%CONTENT%", "a")
							.replace("%CLASS%", " empty"));
					
				} else if (cell.getHover() != null) {
					
					index++;
					String elementclass = BASECLASS + index;
					md = md.replace("%CELL%", CELL_MARKDOWN
							.replace("%CONTENT%", cell.getMessage())
							.replace("%CLASS%", " " + elementclass));
					css.add("." + elementclass + ":after{ content: \"" + cell.getHover() + "\"}");
					
				} else {
					md = md.replace("%CELL%", CELL_MARKDOWN
							.replace("%CONTENT%", cell.getMessage())
							.replace("%CLASS%", ""));
				}
				
				
			}
			md = md.replace("%CELL%","");
		}
		md = md.replace("%ROW%","");

		InputStream mdStream = new ByteArrayInputStream(md.getBytes(StandardCharsets.UTF_8));
		InputStream cssStream = new ByteArrayInputStream(String.join("\n",css).getBytes(StandardCharsets.UTF_8));


		Consumer<MessageCreateSpec> mdSpec = s -> {
			s.setContent("**The markdown:**");
			s.addFile("table.md", mdStream);
		};

		Consumer<MessageCreateSpec> cssSpec = s -> {
			s.setContent("**The CSS:**");
			s.addFile("theme.css", cssStream);
		};

		channel.createMessage(mdSpec).subscribe();
		channel.createMessage(cssSpec).subscribe();
	}
}
