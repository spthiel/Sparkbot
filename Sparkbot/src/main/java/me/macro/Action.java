package me.macro;

@SuppressWarnings("unused")
public enum Action {

	ACHIEVEMENTGET(EffectiveTypes.STRING,EffectiveTypes.O_ITEM),
	ARRAYSIZE(EffectiveTypes.ARRAY,EffectiveTypes.O_VARIABLE),
	BINDGUI(EffectiveTypes.STRING,EffectiveTypes.STRING),
	BIND(EffectiveTypes.SPECIAL_WILDCARD,EffectiveTypes.SPECIAL_WILDCARD),
	BREAK(true),
	CALCYAWTO(EffectiveTypes.INTEGER,EffectiveTypes.INTEGER,EffectiveTypes.O_VARIABLE,EffectiveTypes.O_VARIABLE),
	CAMERA(EffectiveTypes.O_INTEGER),
	CHATFILTER(EffectiveTypes.BOOLEAN),
	CHATHEIGHT(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
	CHATHEIGHTFOCUSED(EffectiveTypes.O_INTEGER,EffectiveTypes.O_TIME),
	CHATOPACITY(EffectiveTypes.O_INTEGER,EffectiveTypes.O_TIME),
	CHATSCALE(EffectiveTypes.O_INTEGER,EffectiveTypes.O_TIME),
	CHATVISIBLE(EffectiveTypes.BOOLEAN),
	CHATWIDTH(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
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
	FOV(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
	GAMMA(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
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
	LOOK(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER,EffectiveTypes.O_TIME),
	LOOKS(EffectiveTypes.INTEGER,EffectiveTypes.O_INTEGER,EffectiveTypes.O_TIME),
	LOTGO(EffectiveTypes.STRING,EffectiveTypes.STRING),
	LCASE(EffectiveTypes.STRING,EffectiveTypes.O_VARIABLE),
	LOOP(true),
	MATCH(EffectiveTypes.STRING,EffectiveTypes.STRING,EffectiveTypes.SPEICAL_MATCH,EffectiveTypes.O_INTEGER,EffectiveTypes.O_BOOLEAN),
	MODIFY(EffectiveTypes.STRING),
	MUSIC(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
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
	SENSITIVITY(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
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
	UNSAFE(EffectiveTypes.INTEGER),
	UNSET(EffectiveTypes.SPECIAL_WILDCARD),
	UNSPRINT(),
	UNTIL(EffectiveTypes.EXPRESSION),
	VOLUME(EffectiveTypes.INTEGER,EffectiveTypes.O_TIME),
	WAIT(EffectiveTypes.TIME),
	WHILE(EffectiveTypes.EXPRESSION)
	;
	
	private EffectiveTypes[] args;
	private boolean noBrackets;
	
	Action(boolean bool) {
		noBrackets = true;
	}
	
	Action(EffectiveTypes... args) {
		this.args = args;
		noBrackets = false;
	}
	
	public EffectiveTypes[] getArgs() {
		return args;
	}
	
	public boolean requiresBrackets() {
		return !noBrackets;
	}

	public static Action getAction(String cmd) {
		for(Action a : Action.values())
			if(a.toString().equalsIgnoreCase(cmd))
				return a;
		return null;
	}
	
}
