package me.macro;

public class MacroException extends Exception {

	private MacroExceptionTypes type;
	
	public MacroException(MacroExceptionTypes exception) {
		super(exception.toString());
		type = exception;
	}
	
	public MacroExceptionTypes getType() {
		return type;
	}
	
	public enum MacroExceptionTypes {
		
		TOO_MANY_EXPRESSIONS,
		NOT_AN_EXPRESSION,
		UNKNOWN,
		NOT_A_VALID_ARGUMENT;
		
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
