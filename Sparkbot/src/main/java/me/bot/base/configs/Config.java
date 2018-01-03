package me.bot.base.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

	private HashMap<String, Object> config;

	public Config() {

	}

	public Config(List<String> file) {

		Pattern p = Pattern.compile("(.+?):(.+?)");

		for (String s : file) {
			Matcher m = p.matcher(s);

			while (m.find()) {
				String key = m.group(1).replace("%pp", ":").replace("%esc", "%");
				String value = m.group(2);

				value = value.replace("%pp", ":");

				config.put(key,getObject(value));

			}

		}

	}

	public Object getValue(String key) {
		return config.get(key);
	}

	public void addOrResetKey(String key,Object value) {
		config.put(key,value);
	}

	public void removeKey(String key) {
		config.remove(key);
	}

	private Object getObject(String value) {

		if (value.matches("\\d+")) {

			long toPut = Long.parseLong(value);
			return toPut;

		} else if (value.matches("\\d+\\.\\d+")) {

			double toPut = Double.parseDouble(value);
			return toPut;

		} else if (value.matches("true|false")) {

			return Boolean.valueOf(value);

		} else if (value.matches("\\[.*?\\]")) {

			Object[] array = value.substring(1, value.length() - 1).split(",");

			for (int i = 0; i < array.length; i++) {
				array[i] = getObject(((String) array[i]).replace("%com",",").replace("%esc","%"));
			}

			return value;

		} else {

			return value.replace("%esc", "%");

		}

	}

	private String stringify ( Object object) {

		if(object instanceof Object[]) {
			StringBuilder builder = new StringBuilder();
			Arrays.asList((Object[])object).forEach(objects -> builder.append("\"" + objects.toString().replace("%","%esc").replace(":","%pp").replace(",","%com") + "\","));
			String toPut = builder.toString();
			return "[" + toPut.substring(0,toPut.length()-1) + "]";
		} else {

			return object.toString().replace("%","%esc").replace(":","%pp");

		}

	}

	public List<String> toFileFormat() {
		ArrayList<String> out = new ArrayList<>();
		ArrayList<String> toSort = new ArrayList<>(config.keySet());
		for (String key : toSort) {
			String toAdd = key.replace("%", "%esc").replace(":", "%pp");
			Object store = config.get(key);
			if (!(store instanceof String[]))
				toAdd += ":" + stringify(config.get(key));
		}

		return out;
	}

}
