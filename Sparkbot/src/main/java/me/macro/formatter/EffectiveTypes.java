package me.macro.formatter;

import me.macro.formatter.MacroException.MacroExceptionTypes;

public enum EffectiveTypes {

	STRING(DataTypes.STRING,false),
	INTEGER(DataTypes.INTEGER,false),
	EXPRESSION(DataTypes.EXPRESSION,false),
	BOOLEAN(DataTypes.BOOLEAN,false),
	ITEM(DataTypes.ITEM,false),
	VARIABLE(DataTypes.VARIABLE,false),
	TIME(DataTypes.TIME,false),
	FILE(DataTypes.FILE,false),
	PARAMETER(DataTypes.PARAMETER,false),
	ARRAY(DataTypes.ARRAY,false),

	O_STRING(DataTypes.STRING,true),
	O_INTEGER(DataTypes.INTEGER,true),
	O_EXPRESSION(DataTypes.EXPRESSION,true),
	O_BOOLEAN(DataTypes.BOOLEAN,true),
	O_ITEM(DataTypes.ITEM,true),
	O_VARIABLE(DataTypes.VARIABLE,true),
	O_TIME(DataTypes.TIME,true),
	O_ARRAY(DataTypes.ARRAY,true),
	BUTTON(DataTypes.BUTTON,true),

	SPECIAL_MATCH("match",false),
	SPECIAL_FOREACH("foreach",false),
	SPECIAL_WILDCARD("wildcard",false),
	;

	private DataTypes type;
	private String specialType;
	private boolean optional;

	EffectiveTypes(DataTypes type,boolean optional) {
		this.type = type;
		this.optional = optional;
		this.specialType = "";
	}

	EffectiveTypes(String name,boolean optional) {
		specialType = name;
		this.optional = optional;
	}

	public String format(String s) throws MacroException {
		s = s.trim();
		if(this.specialType.equalsIgnoreCase("wildcard"))
			return s;
		if(this.specialType.equalsIgnoreCase("foreach"))
			return s;
		if(this.specialType.equalsIgnoreCase("match"))
			return s;
		if(optional && s.equalsIgnoreCase(""))
			return s;
		else if(!optional && s.equalsIgnoreCase(""))
			throw new MacroException(MacroExceptionTypes.NOT_A_VALID_ARGUMENT);
		return DataTypes.formatData(s, type);
	}

}