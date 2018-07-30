package me.bot.base;

import java.util.HashMap;

public class BotConstruct {
	
	public String
			TOKEN = "TOKEN",
			NAME = "BOT-NAME",
			COMMAND_PACKAGE = "PACKAGE",
			LANGUAGEPATH = "CLASS",
			TWITCHURL = "OPTIONAL";
	
	public HashMap<String,String> apikeys = new HashMap<>();
	
	public BotConstruct() {
		apikeys.put("example1","apikey");
		apikeys.put("example2","apikey2");
	}
	
}
