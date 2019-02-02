package me.macro.formatter;

import me.main.Entry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FormatObject {

	private List<Integer> diff;
	private List<Entry<Integer,MacroException>> exceptions;
	private List<String> formatted;
	private List<Integer> criticalErrors;
	private boolean
		indepthcheck,
		inculdediff;

	public FormatObject(boolean indepthcheck, boolean includediff) {
		formatted = new ArrayList<>();
		criticalErrors = new ArrayList<>();

		this.indepthcheck = indepthcheck;
		if(indepthcheck)
			exceptions = new ArrayList<>();
		else
			exceptions = null;

		this.inculdediff = includediff;
		if(includediff)
			diff = new ArrayList<>();
		else
			diff = null;
	}

	public boolean inculdesDiff() {
		return inculdediff;
	}

	public boolean checksIndepth() {
		return indepthcheck;
	}

	public void addEntry(int line) {
		if(diff != null)
			if(!diff.contains(line))
				diff.add(line);
	}

	public void addCriticalError(int line) {
		criticalErrors.add(line);
	}

	public void addException(int line,MacroException exception) {
		if(exceptions != null)
			exceptions.add(new Entry<>(line,exception));
	}

	public void addLine(String line) {
		formatted.add(line);
	}



	public List<String> getFormatted() {
		return formatted;
	}

	public List<Entry<Integer, MacroException>> getExceptions() {
		return exceptions;
	}

	public List<Integer> getChangedLines() {
		return diff;
	}

	public List<Integer> getCriticalErrors() {
		return criticalErrors;
	}
}