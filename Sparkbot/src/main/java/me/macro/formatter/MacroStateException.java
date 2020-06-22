package me.macro.formatter;

public class MacroStateException extends RuntimeException{
	
	public MacroStateException() {
	
	}
	
	public MacroStateException(String message) {
		
		super(message);
	}
	
	public MacroStateException(String message, Throwable cause) {
		
		super(message, cause);
	}
	
	public MacroStateException(Throwable cause) {
		
		super(cause);
	}
	
	public MacroStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
