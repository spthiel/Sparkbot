package me.macro;

import me.main.Entry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MacroFormatter {

	public static FormatObject format(List<String> input, boolean indepthcheck, boolean includeEditedLines, boolean caps) {

		int tabs = 0;
		int emptyLines = 0;
		int linenumber = 0;
		int skipline = 0;

		FormatObject object = new FormatObject(indepthcheck,includeEditedLines);

		Stack<ControlElement> openelements = new Stack<>();

		for (int i = 0; i < input.size(); i++) {

			if(skipline == 0)
				linenumber++;

			int reltabs = 0;
			String line = input.get(i);

			if(line.trim().equalsIgnoreCase("")) {
				emptyLines++;
				if(emptyLines > 1) {
					input.remove(i);
					i--;
					if(includeEditedLines)
						object.addEntry(linenumber);
					continue;
				}
			} else {
				emptyLines = 0;
			}

			if(line.contains(";")) {
				String[] splitted = line.split(";");
				line = splitted[0];
				if(includeEditedLines)
					object.addEntry(linenumber);
				for(int j = splitted.length-1; j >= 1; j--) {
					if(!splitted[j].trim().equalsIgnoreCase("")) {
						skipline++;
						input.add(i + 1, splitted[j]);
					}
				}
			}

			line = line.trim();

			Entry<String,String> response = parseCommand(line);
			String cmd = response.key;
			if(!cmd.equalsIgnoreCase("")) {
				String cmdtoput = cmd;
				if(caps)
					cmdtoput = cmdtoput.toUpperCase();
				try {
					line = line.substring(0, line.toLowerCase().indexOf(cmd)) + cmdtoput + line.substring(line.toLowerCase().indexOf(cmd) + cmd.length(), line.length());
				} catch(Exception e) {
					System.out.println(line + " " + cmd + " " + cmdtoput);
					e.printStackTrace();
					break;
				}
			}

			if(!openelements.isEmpty() && openelements.peek().isEnd(cmd)) {
				openelements.pop();
				tabs--;
			} else if(!openelements.isEmpty() && openelements.peek().isMiddle(cmd)) {
				reltabs--;
			} else {
				ControlElement element = ControlElement.isStart(cmd);
				if(element != null) {
					openelements.push(element);
					tabs++;
					reltabs--;
				} else if(ControlElement.canBeMiddle(cmd)) {
					object.addCriticalError(linenumber);
				} else if(ControlElement.canBeEnd(cmd)) {
					object.addCriticalError(linenumber);
				}
			}

			if(indepthcheck) {

				Action a = Action.getAction(cmd);
				if(a != null)
					if(!a.requiresBrackets()) {

						String prev = line;
						line = line.replaceAll("\\(.+?$","");
						if(includeEditedLines && !prev.equalsIgnoreCase(line))
							object.addEntry(linenumber);

					} else {

						String argstring = response.value;
						if(argstring != null) {
							EffectiveTypes[] types = a.getArgs();
							String prev = line;
							List<String> args = parseArgs(line.replaceAll(".+?\\((.+)\\)", "$1"));
							for (int i1 = 0; i1 < types.length; i1++) {
								EffectiveTypes t = types[i1];
								try {
									args.set(i1, t.format(args.get(i1)));
								} catch (IndexOutOfBoundsException e1) {
									try {
										t.format("");
									} catch (MacroException e2) {
										object.addException(linenumber, e2);
									}
								} catch (MacroException e) {
									object.addException(linenumber, e);
								}
							}
							String cmdtoput = cmd;
							if (caps)
								cmdtoput = cmdtoput.toUpperCase();
							line = cmdtoput + "(" + String.join(",", args) + ")";
							if (includeEditedLines && !prev.equalsIgnoreCase(line))
								object.addEntry(linenumber);
						} else {
							String cmdtoput = cmd;
							switch(cmdtoput) {
								case "if":
								case "toggle":
									cmdtoput = cmdtoput + "(flag)";
									break;
								case "stop":
									cmdtoput = cmdtoput + "()";
							}
							line = cmdtoput;

						}

					}
			}

			line = lengthen(line,tabs+reltabs);

			object.addLine(line);
		}

		return object;
	}

	private static List<String> parseArgs(String s) {
		List<String> out = new ArrayList<>();
		StringBuilder currentArg = new StringBuilder();
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
				case '"':
					do {
						currentArg.append(c);
						i++;
					} while (i < chars.length && (c = chars[i]) != '"');
					if(i == chars.length-1)
						currentArg.append("\"");
					else
						currentArg.append(c);
					break;
				case '{':
					do {
						currentArg.append(c);
						i++;
					} while ((c = chars[i]) != '}');
					currentArg.append(c);
					break;
				case ',':
					out.add(currentArg.toString().trim());
					currentArg = new StringBuilder();
					break;
				default:
					currentArg.append(c);

			}
		}
		if(!currentArg.toString().trim().equalsIgnoreCase(""))
			out.add(currentArg.toString());
		return out;
	}

	private static Entry<String,String> parseCommand(String s) {
//		s = s.toLowerCase();
//		StringBuilder out = new StringBuilder();
//
//		char[] charArray = s.toCharArray();
//		for (int i = 0; i < charArray.length; i++) {
//			char c = charArray[i];
//			if (c == '/') {
//				if (charArray[i + 1] == '/')
//					return "";
//				out = new StringBuilder();
//			} else if((c + "").matches("[ =]")) {
//				do {
//					i++;
//				} while (i < charArray.length && !(charArray[i] + "").matches("[a-z]"));
//				out = new StringBuilder();
//			} else if(c == '(') {
//				break;
//			} else if((c + "").matches("[a-z]")) {
//				out.append(c);
//			} else {
//				do {
//					i++;
//				} while (i < charArray.length && !(charArray[i] + "").matches("[a-z]"));
//				out = new StringBuilder();
//			}
//		}
//
//		return out.toString();

		s = s.toLowerCase();

		if(s.matches(".*?[a-z]+\\(.*\\).*?")) {
			return new Entry<>(s.replaceAll(".*?([a-z]+)\\(.*\\).*?","$1"),s.replaceAll(".*?[a-z]+\\((.*)\\).*?","$2"));
		} else if(s.matches("^[^@%#&]*?([a-zA-Z]+)[^%]*?$")) {
			return new Entry<>(s.replaceAll("^[^@%#&]*?([a-zA-Z]+)[^%]*?$","$1"),null);
		}
		return new Entry<>(null,null);

	}
	
	private static String lengthen(String line,int length){
		
		StringBuilder toAdd = new StringBuilder();
		for(int i = 0; i < length; i++)
			toAdd.append("\t");
		
		return toAdd.toString() + line;
		
	}
	
}
