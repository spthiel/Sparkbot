package me.macro.formatter;

import java.util.LinkedList;
import java.util.List;

import me.main.Entry;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FormatObject {

	private LinkedList<Entry<Integer,MacroException>> exceptions;
	private LinkedList<String>                        formatted;
	private LinkedList<Integer>                       criticalErrors;
	private boolean
													  indepthcheck;

	public FormatObject(boolean indepthcheck) {
		formatted = new LinkedList<>();
		criticalErrors = new LinkedList<>();

		this.indepthcheck = indepthcheck;
		if(indepthcheck)
			exceptions = new LinkedList<>();
		else
			exceptions = null;

	}

	public boolean checksIndepth() {
		return indepthcheck;
	}

	public void addCriticalError(int line) {
		criticalErrors.add(line);
	}

	public void addException(int line,MacroException exception) {
		if(exceptions != null)
			exceptions.add(new Entry<>(line, exception));
	}

	public void addLine(String line) {
		formatted.add(line);
	}

	public LinkedList<String> getFormatted() {
		return formatted;
	}

	public List<Entry<Integer, MacroException>> getExceptions() {
		return exceptions;
	}

	public List<Integer> getCriticalErrors() {
		return criticalErrors;
	}
	
	@Override
	public String toString() {
		
		return "FormatObject{" +
				"exceptions=" + exceptions +
				", formatted=" + formatted +
				", criticalErrors=" + criticalErrors +
				", indepthcheck=" + indepthcheck +
				'}';
	}
}