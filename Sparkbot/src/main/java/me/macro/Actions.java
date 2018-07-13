package me.macro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public enum Actions {

	ACHIEVEMENTGET(EffectiveTypes.STRING,EffectiveTypes.O_ITEM),
	ARRAYSIZE(EffectiveTypes.ARRAY,EffectiveTypes.O_VARIABLE),
	BINDGUI(EffectiveTypes.STRING,EffectiveTypes.STRING),
	BIND(EffectiveTypes.SPECIAL_WILDCARD,EffectiveTypes.SPECIAL_WILDCARD),
	BREAK(true),
	CALCYAWTO(EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE),
	CAMERA(EffectiveTypes.O_INTEGER),
	CHATFILTER(EffectiveTypes.BOOLEAN),
	CHATHEIGHT(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	CHATHEIGHTFOCUSED(EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	CHATOPACITY(EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	CHATSCALE(EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	CHATVISIBLE(EffectiveTypes.BOOLEAN),
	CHATWIDTH(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	CLEARCHAT(),
	CLEARCRAFTING(),
	CRAFT(EffectiveTypes.ITEM,EffectiveTypes.O_INTEGER,EffectiveTypes.O_BOOLEAN),
	CRAFTANDWAIT(EffectiveTypes.ITEM,EffectiveTypes.O_INTEGER,EffectiveTypes.O_BOOLEAN),
	DEC(EffectiveTypes.VARIABLE,EffectiveTypes.O_INTEGER),
	DECODE(EffectiveTypes.STRING,EffectiveTypes.VARIABLE),
	DISCONNECT(),
	DO(EffectiveTypes.O_INTEGER),
	ECHO(EffectiveTypes.STRING),
	ELSE(true),
	ELSEIF(EffectiveTypes.EXPRESSION),
	ENDIF(true),
	ENDUNSAFE(true),
	ENCODE(EffectiveTypes.STRING,EffectiveTypes.VARIABLE),
	EXEC(EffectiveTypes.FILE,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING),
	FILTER(true),
	FOG(EffectiveTypes.O_INTEGER),
	FOR(EffectiveTypes.VARIABLE,EffectiveTypes.INTEGER,EffectiveTypes.INTEGER),
	FOREACH(EffectiveTypes.SPECIAL_FOREACH),
	FOV(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	GAMMA(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	GETID(EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE),
	GETIDREL(EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE),
	GETITEMINFO(EffectiveTypes.ITEM,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE),
	GETPROPERTY(EffectiveTypes.STRING,EffectiveTypes.STRING),
	GETSLOT(EffectiveTypes.INTEGER,EffectiveTypes.VARIABLE,EffectiveTypes.O_INTEGER),
	GETSLOTITEM(EffectiveTypes.INTEGER,EffectiveTypes.VARIABLE,EffectiveTypes.VARIABLE,EffectiveTypes.VARIABLE),
	GUI(EffectiveTypes.STRING),
	IIF(EffectiveTypes.EXPRESSION,EffectiveTypes.STRING,EffectiveTypes.STRING),
	IF(EffectiveTypes.EXPRESSION),
	IFBEGINSWITH(EffectiveTypes.STRING,EffectiveTypes.STRING),
	IFCONTAINS(EffectiveTypes.STRING,EffectiveTypes.STRING),
	IFENDSWITH(EffectiveTypes.STRING,EffectiveTypes.STRING),
	IFMATCHES(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_INTEGER),
	INDEXOF(EffectiveTypes.ARRAY,EffectiveTypes.VARIABLE,EffectiveTypes.SPECIAL_WILDCARD),
	INC(EffectiveTypes.VARIABLE,EffectiveTypes.O_INTEGER),
	INVENTORYDOWN(EffectiveTypes.O_INTEGER),
	INVENTORYUP(EffectiveTypes.O_INTEGER),
	IMPORT(EffectiveTypes.STRING),
	ISRUNNUING(EffectiveTypes.STRING),
	ITEMID(EffectiveTypes.ITEM),
	ITEMNAME(EffectiveTypes.INTEGER),
	JOIN(EffectiveTypes.STRING,EffectiveTypes.ARRAY,EffectiveTypes.O_VARIABLE),
	KEY(EffectiveTypes.SPECIAL_WILDCARD),
	KEYDOWN(EffectiveTypes.SPECIAL_WILDCARD),
	KEYUP(EffectiveTypes.SPECIAL_WILDCARD),
	LOG(EffectiveTypes.STRING),
	LOGRAW(EffectiveTypes.STRING),
	LOOK(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	LOOKS(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	LOTGO(EffectiveTypes.STRING,EffectiveTypes.STRING),
	LCASE(EffectiveTypes.STRING,EffectiveTypes.O_VARIABLE),
	LOOP(true),
	MATCH(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.SPECIAL_MATCH,EffectiveTypes.O_INTEGER,EffectiveTypes.O_BOOLEAN),
	MODIFY(EffectiveTypes.STRING),
	MUSIC(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	NEXT(true),
	PASS(true),
	PICK(EffectiveTypes.ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM,EffectiveTypes.O_ITEM),
	PLACESIGN(EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_BOOLEAN),
	PLAYSOUND(EffectiveTypes.STRING,EffectiveTypes.O_INTEGER),
	POP(EffectiveTypes.ARRAY,EffectiveTypes.VARIABLE),
	POPUPMESSAGE(EffectiveTypes.STRING,EffectiveTypes.O_BOOLEAN),
	PRESS(EffectiveTypes.SPECIAL_WILDCARD),
	PROMPT(EffectiveTypes.VARIABLE,EffectiveTypes.PARAMETER,EffectiveTypes.O_STRING,EffectiveTypes.O_BOOLEAN,EffectiveTypes.O_STRING),
	PUT(EffectiveTypes.ARRAY,EffectiveTypes.VARIABLE),
	PUSH(EffectiveTypes.ARRAY,EffectiveTypes.VARIABLE),
	RANDOM(EffectiveTypes.VARIABLE,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	REGEXREPLACE(EffectiveTypes.VARIABLE,EffectiveTypes.STRING,EffectiveTypes.O_STRING),
	RELOADRESOURCES(true),
	REPLACE(EffectiveTypes.VARIABLE,EffectiveTypes.STRING,EffectiveTypes.O_STRING),
	RESOURCEPACKS(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.STRING),
	RESPAWN(),
	REPL(true),
	SENSITIVITY(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	SET(EffectiveTypes.VARIABLE,EffectiveTypes.SPECIAL_WILDCARD),
	SETLABEL(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.O_STRING),
	SETPROPERTY(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.SPECIAL_WILDCARD),
	SETRES(EffectiveTypes.INTEGER,EffectiveTypes.INTEGER),
	SETSLOTITEM(EffectiveTypes.O_ITEM,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	SHADERGROUP(EffectiveTypes.O_STRING),
	SHOWGUI(EffectiveTypes.STRING,EffectiveTypes.O_BOOLEAN),
	SLOT(EffectiveTypes.O_INTEGER),
	SLOTCLICK(EffectiveTypes.O_INTEGER,EffectiveTypes.BUTTON,EffectiveTypes.BOOLEAN),
	SPLIT(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.O_ARRAY),
	SPRINT(),
	SQRT(EffectiveTypes.INTEGER,EffectiveTypes.O_VARIABLE),
	STRIP(EffectiveTypes.VARIABLE,EffectiveTypes.STRING),
	STOP(EffectiveTypes.O_STRING),
	STORE(EffectiveTypes.SPECIAL_WILDCARD,EffectiveTypes.SPECIAL_WILDCARD),
	STOREOVER(EffectiveTypes.SPECIAL_WILDCARD,EffectiveTypes.SPECIAL_WILDCARD),
	TILEID(EffectiveTypes.ITEM),
	TILENAME(EffectiveTypes.INTEGER),
	TIME(EffectiveTypes.VARIABLE,EffectiveTypes.STRING),
	TITLE(EffectiveTypes.O_STRING,EffectiveTypes.O_STRING,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER,EffectiveTypes.O_INTEGER),
	TOGGLE(EffectiveTypes.O_VARIABLE),
	TOGGLEKEY(EffectiveTypes.SPECIAL_WILDCARD),
	TRACE(EffectiveTypes.INTEGER,EffectiveTypes.BOOLEAN),
	TYPE(EffectiveTypes.STRING),
	UCASE(EffectiveTypes.STRING,EffectiveTypes.O_VARIABLE),
	UNIMPORT(),
	UNSAFE(EffectiveTypes.O_INTEGER),
	UNSET(EffectiveTypes.SPECIAL_WILDCARD),
	UNSPRINT(),
	UNTIL(EffectiveTypes.EXPRESSION),
	VOLUME(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER),
	WAIT(EffectiveTypes.TIME),
	WHILE(EffectiveTypes.EXPRESSION)
	;

	private static Pattern regex;

	private EffectiveTypes[] args;
	private boolean noBrackets;
	private IActionFormatter formatter;

	Actions(IActionFormatter formatter) {
		this.formatter = formatter;
	}

	Actions(boolean bool) {
		noBrackets = true;
	}

	Actions(EffectiveTypes... args) {
		this.args = args;
		noBrackets = false;
	}

	public String format(FormatObject object, int line, String commandWithArgs, boolean uppercase) {

		if(formatter != null)
			return formatter.formatAction(this,object,line,commandWithArgs,uppercase);

		if(!commandWithArgs.toLowerCase().startsWith(name().toLowerCase() + "(")) {
			object.addException(line,new MacroException(MacroException.MacroExceptionTypes.NOT_A_VALID_ARGUMENT));
			return commandWithArgs;
		}

		if(noBrackets)
			return (uppercase ? name().toUpperCase() : name().toLowerCase());

		String argstring = commandWithArgs.substring(name().length()).replaceAll(".*?\\((.*)\\).*?","$1");
		List<String> args = parseArgs(argstring);
		boolean isOkay = false;
		for (int i = 0; i < args.size(); i++) {
			String arg = args.get(i);

		}
		return null;
	}

	public static Actions getAction(String cmd) {
		for(Actions a : Actions.values())
			if(a.toString().equalsIgnoreCase(cmd))
				return a;
		return null;
	}

	public static Actions getActionFromString(String str) {
		Pattern p = getOrGenRegex();
		Matcher m = p.matcher(str.toLowerCase());
		return (m.find() ? getAction(m.group(1)) : null);
	}

	public static Pattern getOrGenRegex() {
		if(regex != null)
			return regex;
		StringBuilder builder = new StringBuilder();
		String seperator = "";
		for(Actions a : values()) {
			builder.insert(0, a.name().toLowerCase() + seperator);
			seperator = "|";
		}

		return (regex = Pattern.compile("(?:[^@&#]\\b|=|^)(" + builder.toString() + ")(?![a-zA-Z%])([(;]|)", Pattern.CASE_INSENSITIVE));
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
					} while (i < chars.length && (c = chars[i]) != '"');
					if(i == chars.length-1)
						currentArg.append("\"");
					else
						currentArg.append(c);
					break;
				case '{':
					do {
						currentArg.append(c);
						i++;
					} while ((c = chars[i]) != '}');
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

}