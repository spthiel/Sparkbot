package me.main.utils;

import reactor.core.Exceptions;

public class ExceptionUtils {
	
	public static String makePrintAble(Exception e) {
		
		System.out.println("e: " + e);
		System.out.println("e.getClass().getName(): " + e.getClass().getName());
		System.out.println("e.getMessage(): " + e.getMessage());
		
		StringBuilder s = new StringBuilder();
		
		if (Exceptions.isBubbling(e)) {
			s.append(e.getMessage()).append("\n");
		} else {
			s.append(e).append("\n");
		}
		
		StackTraceElement[] stackTrace = e.getStackTrace();
		
		int skip = 0;
		
		for (int i = stackTrace.length - 1 ; i >= 0 ; i--) {
			StackTraceElement element = stackTrace[i];
			
			String classname = element.getClassName();
			if (!classname.startsWith("me.")) {
				skip++;
				continue;
			}
			
			if (skip > 0) {
				s.append("    ... (").append(skip).append(")\n");
				skip = 0;
			}
			
			s
				.append("    at: ")
				.append(element.getClassName()).append("#").append(element.getLineNumber())
				.append(i > 0 ? "\n" : "");
		}
		
		return s.toString();
	}
	
}
