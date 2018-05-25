package me.macro;

import me.main.Entry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MacroFormatter {

	public static List<String> format(List<String> input) {

		int tabs = 0;
		int emptyLines = 0;

		List<String> out = new ArrayList<>();

		Stack<ControlElement> openelements = new Stack<>();

		for (int i = 0; i < input.size(); i++) {


			int reltabs = 0;
			String line = input.get(i);

			if(line.trim().equalsIgnoreCase("")) {
				emptyLines++;
				if(emptyLines > 1) {
					input.remove(i);
					i--;
					continue;
				}
			} else {
				emptyLines = 0;
			}

			if(line.contains(";")) {
				String[] splitted = line.split(";");
				line = splitted[0];
				for(int j = splitted.length-1; j >= 1; j--) {
					if(!splitted[j].trim().equalsIgnoreCase("")) {
						input.add(i + 1, splitted[j]);
					}
				}
			}

			line = line.trim();

			if(!openelements.isEmpty() && openelements.peek().isEnd(line)) {
				openelements.pop();
				tabs--;
			} else if(!openelements.isEmpty() && openelements.peek().isMiddle(line)) {
				reltabs--;
			} else {
				ControlElement element = ControlElement.isStart(line);
				if(element != null) {
					openelements.push(element);
					tabs++;
					reltabs--;
				}
			}

			line = lengthen(line,tabs+reltabs);

			out.add(line);
		}

		return out;
	}

	private static String lengthen(String line,int length){

		StringBuilder toAdd = new StringBuilder();
		for(int i = 0; i < length; i++)
			toAdd.append("\t");

		return toAdd.toString() + line;

	}
	
}
