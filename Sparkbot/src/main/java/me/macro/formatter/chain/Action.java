package me.macro.formatter.chain;

public class Action {
	
	private final boolean brackets;
	private final String toString;
	
	public Action(boolean brackets, String toString) {
		
		this.brackets = brackets;
		this.toString = toString;
	}
	
	public boolean hasBrackets() {
		return brackets;
	}
	
	@Override
	public String toString() {
		
		return toString;
	}
}
