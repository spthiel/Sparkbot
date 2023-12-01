package me.main;

import java.io.PrintStream;

public class Printer extends PrintStream {

	private final PrintStream norm;
	private final PrintStream file;

	public Printer(PrintStream out,PrintStream file) {
		super(out);
		this.norm = out;
		this.file = file;
	}

	@Override
	public void println(String x) {
		norm.println(x);
		file.println(x);
	}
}
