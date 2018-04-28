package me.test;

public class TestRunnable implements Runnable{

	private String message;

	public TestRunnable(String message) {
		this.message = message;
	}

	@Override
	public void run() {
		System.out.println(message);
	}
}
