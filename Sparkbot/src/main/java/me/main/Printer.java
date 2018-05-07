package me.main;

import java.io.OutputStream;
import java.io.PrintStream;

public class Printer extends PrintStream {

	public Printer(OutputStream out) {
		super(out);
	}

}
