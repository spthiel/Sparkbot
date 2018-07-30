package me.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HastebinUtils {
	
	private static String BASE_URL = "https://hastebin.com";
	
	public static Optional<String> getUrl(String id) {
		if(id == null)
			return Optional.empty();
		return Optional.of(BASE_URL + "/" + id);
	}
	
	public static String postCode(List<String> code) {
		return postCode(String.join("\n",code));
	}
	
	public static String postCode(String code) {
		
		try {
			return HTTP.post(BASE_URL + "/documents",code).replaceAll("\\{\"key\":\"(.+?)\"}","$1");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
