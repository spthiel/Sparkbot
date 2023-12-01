package me.bot.base;

import java.util.HashMap;

@SuppressWarnings({"CanBeFinal", "SpellCheckingInspection"})
public class BotConstruct {
	
	public String
			TOKEN = "TOKEN",
			NAME = "BOT-NAME",
			COMMAND_PACKAGE = "PACKAGE",
			LANGUAGEPATH = "CLASS",
			TWITCHURL = "OPTIONAL";
	
	public HashMap<String,String> apiKeys = new HashMap<>();
	
	public BotConstruct() {
		apiKeys.put("example1", "apikey");
		apiKeys.put("example2", "apikey2");
	}
	
}
