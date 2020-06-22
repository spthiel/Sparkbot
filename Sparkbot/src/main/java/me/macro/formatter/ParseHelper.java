package me.macro.formatter;

import me.macro.formatter.chain.Chain;

public class ParseHelper {
	
	private final char[] input;
	private final Chain  chain;
	
	public ParseHelper(char[] input) {
	
		this.input = input;
		this.chain = new Chain();
		parse();
	}
	
	private void parse() {
		
		StringBuilder current = new StringBuilder();
		boolean canHasEquals = true;
		boolean hasEquals = false;
		boolean inString = false;
		int inSpecial = 0;
		int openBrackets = 0;
		for(int i = 0; i < input.length; i++) {
			char c = input[i];
			if(c == '\r') {
				continue;
			}
			if(inString) {
				if(c == '\'') {
					i++;
					if(i < input.length) {
						current.append(input[i]);
					}
				} else if(c == '"'){
					inString = false;
				}
				current.append(c);
				continue;
			}
			if(inSpecial > 0) {
				if(c == '{') {
					inSpecial++;
				} else if(c == '}'){
					inSpecial--;
				}
				current.append(c);
				continue;
			}
			if(c == '\n' || c == ';' || i+1 == input.length) {
				if(i+1 == input.length) {
					current.append(c);
				}
				String currentString = current.toString().trim();
				if(currentString.length() != 0) {
					if (hasEquals) {
						chain.setLeftside(currentString);
					} else if(current.toString().trim().startsWith("$$") || current.toString().trim().equalsIgnoreCase("}$$")) {
						chain.setMeta(currentString);
					} else {
						chain.setAction(false, currentString);
					}
				}
				chain.endLine();
				current = new StringBuilder();
				hasEquals = false;
				canHasEquals = true;
				continue;
			}
			if(c == ')' && openBrackets > 0) {
				openBrackets--;
				if(openBrackets == 0) {
					chain.addParameter(current.toString());
					current = new StringBuilder();
					continue;
				}
			}
			if(c == ',' && openBrackets > 0) {
				chain.addParameter(current.toString());
				current = new StringBuilder();
				continue;
			}
			if(c == '(') {
				openBrackets++;
				if(openBrackets == 1) {
					canHasEquals = false;
					chain.setAction(true, current.toString());
					current = new StringBuilder();
					continue;
				}
			}
			if(canHasEquals && c == '=') {
				canHasEquals = false;
				hasEquals = true;
				chain.setVariable(current.toString());
				current = new StringBuilder();
				continue;
			}
			if(c == '"') {
				inString = true;
			}
			if(openBrackets == 1 && c == '{') {
				inSpecial++;
			}
			current.append(c);
		}
	}
	
	public Chain getChain() {
		
		return chain;
	}
}
