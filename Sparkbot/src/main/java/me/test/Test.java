package me.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.main.utils.BotsOnDiscordUtils;
import me.main.utils.HastebinUtils;

public class Test {

	public static void main(String[] args) {
		
		/*ObjectMapper mapper = new ObjectMapper();
		try {
			Tester start = new Tester();
			String json = mapper.writeValueAsString(start);
			System.out.println("Json: " + json);
			Tester test = mapper.readValue(json,new TypeReference<Tester>(){});
			System.out.println("[Print] " + test);
			
			//start.setA("not a");
			json = "{\"a\":\"b\",\"c\":null,\"bool\":false,\"x\":42}";
			System.out.println("Json2: " + json);
			test = mapper.readValue(json,new TypeReference<Tester>(){});
			System.out.println("[Print2] " + test);
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		//BotsOnDiscordUtils.testPayLoad("398040194995060736",9,"21043483d7203c4d32d9fbabc37eaf6b");
		
		//System.out.println(HastebinUtils.postCode("test"));
		//System.out.println(HastebinUtils.getUrl(null).orElse("null"));
		
	}
	
	private static Optional<TestMember> getArgMember(List<TestMember> members, String arg) {
		TestMember contain = null;
		TestMember containInsensitive = null;
		for(TestMember member : members) {
			String discrim = "#" + member.getDiscriminator();
			String normalname = member.getUsername();
			String name = member.getDisplayName();
			if((normalname + discrim).equalsIgnoreCase(arg) || (name + discrim).equalsIgnoreCase(arg)) {
				return Optional.of(member);
			}
			
			if(normalname.equalsIgnoreCase(arg) || name.equalsIgnoreCase(arg)) {
				return Optional.of(member);
			}
			
			if(normalname.contains(arg) || name.contains(arg)) {
				if (contain == null) {
					contain = member;
				}
			} else if(normalname.toLowerCase().contains(arg.toLowerCase()) || name.toLowerCase().contains(arg.toLowerCase())) {
				if (containInsensitive == null) {
					containInsensitive = member;
				}
			}
		}
		if(contain != null) {
			System.out.println("contain");
			return Optional.of(contain);
		} else if (containInsensitive != null) {
			System.out.println("insensitive");
			return Optional.of(containInsensitive);
		}
		else
			return Optional.empty();
	}
	
	private static void splitTest() {
		
		String smallString = generateString(100);
		String mediumString = generateString(40000);
		String largeString = generateString(1000000);
		
		Pattern p = Pattern.compile("((?:..|.\n|\n.|\n\n){1,1000})");
		
		for(int i = 0; i <= 2; i++) {
			
			String str;
			
			if(i == 0) {
				System.out.println("Small string (" + smallString.length() + " chars)");
				str = smallString;
			} else if(i == 1) {
				System.out.println("Medium string (" + mediumString.length() + " chars)");
				str = mediumString;
			} else {
				System.out.println("Large string (" + largeString.length() + " chars)");
				str = mediumString;
			}
			
			List<String> segments = new ArrayList<>();
			long startPattern = System.nanoTime();
			
			Matcher m = p.matcher(str);
			while (m.find())
				segments.add(m.group(1));
			
			System.out.println("Pattern segments took \t\t" + (System.nanoTime()-startPattern) + "ns");
			
			// -----------------------------------------
			
			segments = new ArrayList<>();
			long startSubstring = System.nanoTime();
			
			while(str.length() > 2000){
				segments.add(str.substring(0,2000));
				str = str.substring(2001, str.length());
			}
			
			System.out.println("Substring segments took \t" + (System.nanoTime()-startPattern) + "ns");
		}
	}

	private static String generateString(int length) {
		StringBuilder builder = new StringBuilder();
		while(length > 0) {
			builder.append("a");
			length--;
		}
		return builder.toString();
	}

}
