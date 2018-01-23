package me.main;

import me.bot.base.configs.ResourceManager;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args ){

		ResourceManager testmanager = new ResourceManager("./resources/");
		JSONObject test = testmanager.getConfig("configs/main","botowner.json");
		test.put("test",testmanager);
		System.out.println(test.toString());
		JSONObject test2 = new JSONObject(test.toString());
	}
}
