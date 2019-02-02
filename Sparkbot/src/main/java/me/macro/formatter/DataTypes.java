package me.macro.formatter;

import me.macro.formatter.MacroException.MacroExceptionTypes;

public enum DataTypes {

	STRING(),
	INTEGER(),
	EXPRESSION(),
	BOOLEAN(),
	ITEM(),
	VARIABLE(),
	TIME(),
	BUTTON(),
	FILE(),
	PARAMETER(),
	ARRAY();

	public static String formatData(String arg,DataTypes type) throws MacroException {

		switch(type) {
			case STRING:
				if(arg.startsWith("\""))
					arg = arg.substring(1);
				if(arg.endsWith("\""))
					arg = arg.substring(0, arg.length()-1);
				if(parseVariableValue(arg,STRING))
					return "\"" + arg + "\"";
				return "\"" + arg.replaceAll("([^\\\\]|^)\"", "$1\\\\\"") + "\"";
			case INTEGER:
				if(parseVariableValue(arg,INTEGER))
					return arg;
				if(!arg.matches("\\d+"))
					arg = parseInt(arg);
				return arg;
			case EXPRESSION:
				return parseExpression(arg);
			case BOOLEAN:
				if(parseVariableValue("^%@?[a-z_0-9]+%$",BOOLEAN))
					return arg;
				return formatExpressionPart(arg, false);
			case ITEM:
				if(parseVariableValue(arg,STRING))
					return arg;
				if(arg.startsWith("\""))
					arg = arg.substring(1);
				if(arg.endsWith("\""))
					arg = arg.substring(0, arg.length()-1);
				if(arg.matches("^[a-z_0-9]+(?::\\d+)?$"))
					return "\"" + arg.replaceAll("([^\\\\]|^)\"", "$1\\\\\"") + "\"";
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			case VARIABLE:
				if(arg.matches("^@?(?:#|&|)[a-z_0-9]+$"))
					return arg;
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			case TIME:
				if(arg.matches("^(?:\\d+(?:ms|t|)|\\d+\\.\\d+)?"))
					return arg;
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			case BUTTON:
				if(arg.matches("RMB"))
					return arg;
				return "";
			case FILE:
				if(arg.matches("^.+\\.txt$"))
					return arg;
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			case PARAMETER:
				if(arg.matches("^\"?\\$\\$\\[.+?]\"?$"))
					return arg.matches("^\".+\"$") ? arg : "\"" + arg + "\"";
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			case ARRAY:
				if(arg.matches("^(?:@|)(?:&|#|)[a-z_0-9]+\\[]$"))
					return arg;
				else if(arg.matches("^(?:@|)(?:&|#|)[a-z_0-9]+$"))
					return arg + "[]";
				else
					throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
			default:
				return arg;
		}

	}

	private static String parseInt(String s) {
		StringBuilder out = new StringBuilder();
		for(char c : s.toCharArray())
			if((c + "").matches("\\d"))
				out.append(c);
		return out.toString();
	}

	private static String parseExpression(String s) throws MacroException{
		char[] chars = s.toCharArray();
		StringBuilder out = new StringBuilder();

		StringBuilder lastExpression = new StringBuilder();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch(c) {
				case '&':
					if(chars[i+1] == '&')
						i++;
					out.append("(").append(formatExpression(lastExpression.toString())).append(") && ");
					lastExpression = new StringBuilder();
					break;
				case '%':
					i++;
					c = chars[i];
					do {
						lastExpression.append(c);
						i++;
					} while((c = chars[i]) != '%');
					break;
				case '"':
					do {
						lastExpression.append(c);
						i++;
					} while((c = chars[i]) != '"');
					lastExpression.append(c);
					break;
				case '|':
					if(chars[i+1] == '|')
						i++;
					out.append("(").append(formatExpression(lastExpression.toString())).append(") || ");
					lastExpression = new StringBuilder();
					break;
				case '(':
					StringBuilder eatExpression = new StringBuilder();
					i++;
					c = chars[i];
					if(chars[i] != ')') {
						do {
							eatExpression.append(c);
							i++;
						} while((c = chars[i]) != ')');
						out.append("(").append(parseExpression(eatExpression.toString())).append(")");
					}
					break;
				default:
					lastExpression.append(c);
			}
		}
		if(!lastExpression.toString().trim().equalsIgnoreCase(""))
			if(out.length() > 0)
				out.append("(").append(formatExpression(lastExpression.toString())).append(")");
			else
				out.append(formatExpression(lastExpression.toString()));
		return out.toString();
	}

	private static String formatExpression(String s) throws MacroException {
		if(s.contains("=")) {
			String[] splitted = s.split("={1,2}");
			if(splitted.length == 2) {
				s = formatExpressionPart(splitted[0],true) + " == " + formatExpressionPart(splitted[1],true);
			} else {
				throw new MacroException(MacroExceptionTypes.TOO_MANY_EXPRESSIONS);
			}
		} else {
			s = formatExpressionPart(s,false);
		}
		return s;
	}

	private static String formatExpressionPart(String s,boolean inclString) throws MacroException{
		s = s.trim();
		if(s.startsWith("\"") && inclString) {
			return s;
		} else if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
			return s;
		} else if(s.equalsIgnoreCase("0")) {
			return "false";
		} else if(s.equalsIgnoreCase("1")) {
			return "true";
		} else {
			if(s.toUpperCase().equals(s)) {
				if(s.contains("%"))
					return s.replace("%", "");
				else
					return s;
			} else if(s.toLowerCase().equals(s)) {
				return s;
			} else {
				throw new MacroException(MacroExceptionTypes.NOT_AN_EXPRESSION);
			}
		}
	}

	private static String parseVariable(String s) {

		StringBuilder out = new StringBuilder();
		if(s.startsWith("@")) {
			out.append("@");
			s = s.substring(1);
		}
		if(s.startsWith("&")) {
			out.append("&");
			s = s.substring(1);
		} else if(s.startsWith("#")) {
			out.append("#");
			s = s.substring(1);
		}
		char[] chars = s.toCharArray();

		StringBuilder lastExpression = new StringBuilder();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch(c) {

			}
		}
		return out.toString();
	}

	private static boolean parseVariableValue(String s,DataTypes type) {

		if(type == INTEGER && s.matches("\\d+"))
			return true;

		if(s.startsWith("%") && s.endsWith("%")) {
			s = s.substring(1,s.length()-1);
		} else {
			return false;
		}

		if(s.startsWith("@"))
			s = s.substring(1);

		switch(type) {
			case STRING:
				if(s.startsWith("&"))
					s = s.substring(1);
				else
					return false;
				break;
			case INTEGER:
				if(s.startsWith("#"))
					s = s.substring(1);
				else
					return false;
				break;
			case BOOLEAN:
				if(s.startsWith("&") || s.startsWith("#"))
					return false;
				break;
		}

		if(s.matches("[a-z_0-9]+\\[.+?]")) {
			return parseVariableValue(s.replaceAll("[a-z_0-9]+\\[(.+?)]","$1"),INTEGER);
		}

		return true;
	}

}