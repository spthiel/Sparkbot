package me.macro.formatter.chain;

import me.macro.formatter.MacroStateException;

public enum VariableType {
	
	BOOL, STRING, INT;
	
	public static VariableType typeFromChar(char c) {
		if(c == '#') {
			return INT;
		}
		if(c == '&') {
			return STRING;
		}
		if((c + "").matches("[a-z]")) {
			return BOOL;
		}
		throw new MacroStateException("Illegal State for Variable: " + c);
	}
	
}
