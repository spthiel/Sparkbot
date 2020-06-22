package me.macro.formatter.chain;

public class Variable {
	
	private final boolean global;
	private final VariableType type;
	private final String toString;
	
	public Variable(String toString) {
		
		int typePos = 0;
		this.global = toString.startsWith("@");
		if(this.global) {
			typePos++;
		}
		this.type = VariableType.typeFromChar(toString.charAt(typePos));
		this.toString = toString;
	}
	
	public boolean isGlobal() {
		
		return global;
	}
	
	public VariableType getType() {
		
		return type;
	}
	
	@Override
	public String toString() {
		
		return toString;
	}
}
