package me.macro.formatter.chain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Chain implements Iterable<Line> {
	
	private final LinkedList<Line> lines       = new LinkedList<>();
	private       Line             currentLine = new Line();
	
	public Chain() {
	
	}
	
	public void setVariable(String var) {
		currentLine.setVariable(var.trim());
	}
	
	public void setAction(boolean brackets, String action) {
		currentLine.setAction(brackets, action.trim());
	}
	
	public void setLeftside(String left) {
		currentLine.setRightside(left.trim());
	}
	
	public void setMeta(String meta) {
		currentLine.setMeta(meta.trim());
	}
	
	public void addParameter(String parameter) {
		currentLine.addParameter(parameter.trim());
	}
	
	public void endLine() {
		if(currentLine.isEmpty()) {
			lines.add(new Line());
		} else {
			lines.add(currentLine);
			currentLine = new Line();
		}
	}
	
	@Override
	public String toString() {
		
		return "Chain{" +
				"lines=" + toString(lines) +
				", currentLine=" + currentLine +
				'}';
	}
	
	private String toString(LinkedList<?> list) {
		StringBuilder listString = new StringBuilder();
		
		for (Object s : list)
		{
			listString.append(s).append(",\n");
		}
		return listString.toString();
	}
	
	@Override
	public Iterator<Line> iterator() {
		
		return lines.iterator();
	}
	
	@Override
	public void forEach(Consumer<? super Line> action) {
		lines.forEach(action);
	}
	
	@Override
	public Spliterator<Line> spliterator() {
		
		return lines.spliterator();
	}
}
