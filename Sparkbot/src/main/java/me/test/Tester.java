package me.test;

public class Tester {
	
	public String
		a,
		b,
		c;
	
	public boolean
		bool = false;
	
	public int
		x = 42;
	
	public Tester() {
		c = "3";
	}
	
	@Override
	public String toString() {
		
		return "Tester: " + a + " " + b + " " + c + " " + bool + " " + x;
	}
}
