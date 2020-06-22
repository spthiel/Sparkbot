package me.macro.formatter.chain;

import java.util.ArrayList;

import me.macro.formatter.MacroStateException;

public class Line {
	
	private       Variable variable;
	private       Action                                action;
	private       String                                rightside;
	private       String                                meta;
	private final ArrayList<String>                     parameters = new ArrayList<>();
	
	//region Variable
	public Variable getVariable() {
		
		return variable;
	}
	
	void setVariable(String variable) {
		
		if (this.isMeta()) {
			throw new MacroStateException("Invalid State: " + this + " on setVariable with " + variable);
		}
		this.variable = new Variable(variable);
	}
	
	public boolean hasVariable() {
		
		return variable != null;
	}
	
	//endregion
	//region Action
	public Action getAction() {
		
		return action;
	}
	
	void setAction(boolean brackets, String action) {
		
		if (isMeta() || hasRightside()) {
			throw new MacroStateException("Invalid state: " + this + " on setAction with " + action);
		}
		this.action = new Action(brackets, action);
	}
	
	public boolean hasAction() {
		
		return action != null;
	}
	
	//endregion
	//region Righside
	public String getRightside() {
		
		return rightside;
	}
	
	void setRightside(String rightside) {
		
		if (this.isMeta() || this.hasAction() || !this.hasVariable()) {
			throw new MacroStateException("Invalid state: " + this + " on setRightside with " + rightside);
		}
		this.rightside = rightside;
	}
	
	public boolean hasRightside() {
		
		return this.rightside != null;
	}
	
	//endregion
	//region Parameters
	public ArrayList<String> getParameters() {
		
		return parameters;
	}
	
	void addParameter(String parameter) {
		
		if (isMeta() || !hasAction() || hasRightside()) {
			throw new MacroStateException("Invalid state: " + this + " on addParameter with " + parameter);
		}
		this.parameters.add(parameter);
	}
	
	public boolean hasParameters() {
		
		return parameters.size() > 0;
	}
	
	//endregion
	//region Meta
	public String getMeta() {
		
		return meta;
	}
	
	void setMeta(String meta) {
		
		if (hasVariable() || hasRightside() || hasAction() || hasParameters()) {
			throw new MacroStateException("Invalid state: " + this + " on setMeta with " + meta);
		}
		this.meta = meta;
	}
	
	public boolean isMeta() {
		
		return meta != null;
	}
	//endregion
	
	public boolean isEmpty() {
		
		return variable == null && action == null && rightside == null && meta == null && parameters.size() == 0;
	}
	
	@Override
	public String toString() {
		
		return "Line{" +
				(variable == null ? "" : "variable='" + variable + "', ") +
				(action == null ? "" : "action='" + action + "', ") +
				(rightside == null ? "" : "leftside='" + rightside + "', ") +
				(meta == null ? "" : "meta='" + meta + "', ") +
				(parameters.size() == 0 ? "" : "parameters=" + parameters + "(" + parameters.size() + ")") +
				'}';
	}
}
