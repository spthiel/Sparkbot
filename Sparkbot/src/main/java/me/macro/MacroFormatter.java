package me.macro;

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

			String cmd = parseCommand(line);
			if(!cmd.equalsIgnoreCase("")) {
				String cmdtoput = cmd;
				if(caps)
					cmdtoput = cmdtoput.toUpperCase();
				line = cmdtoput + line.substring(line.toLowerCase().indexOf(cmd)+cmd.length(), line.length());
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

						EffectiveTypes[] types = a.getArgs();
						String prev = line;
						List<String> args = parseArgs(line.replaceAll(".+?\\((.+)\\)","$1"));
						for (int i1 = 0; i1 < types.length; i1++) {
							EffectiveTypes t = types[i1];
							try {
								args.set(i1, t.format(args.get(i1)));
							} catch (IndexOutOfBoundsException e1) {
								try {
									t.format("");
								} catch(MacroException e2) {
									object.addException(linenumber,e2);
								}
							} catch (MacroException e) {
								object.addException(linenumber,e);
							}
						}
						String cmdtoput = cmd;
						if(caps)
							cmdtoput = cmdtoput.toUpperCase();
						line = cmdtoput + "(" + String.join(", ", args) + ")";
						if(includeEditedLines && !prev.equalsIgnoreCase(line))
							object.addEntry(linenumber);

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
					} while((c = chars[i]) != '"');
					currentArg.append(c);
					break;
				case '{':
					do {
						currentArg.append(c);
						i++;
					} while((c = chars[i]) != '}');
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

	private static String parseCommand(String s) {
		s = s.toLowerCase();
		StringBuilder out = new StringBuilder();
		
		for(char c : s.toCharArray()) {
			if(!(c + "").matches("[a-z]"))
				break;
			else
				out.append(c);
		}
		
		return out.toString();
	}
	
	private static String lengthen(String line,int length){
		
		StringBuilder toAdd = new StringBuilder();
		for(int i = 0; i < length; i++)
			toAdd.append("\t");
		
		return toAdd.toString() + line;
		
	}
	
	private static void saveFile(String path,List<String> toStore) {

		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {

			toStore.forEach(line -> {
				try {
					writer.write(line + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	private static ArrayList<String> readFile(String path){

		ArrayList<String> out = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "UTF8"))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				out.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
}
