package me.tablecreator;

public class Cell {

	private String message,hover;

	public Cell() {
		this.message = null;
		this.hover = null;
	}

	public Cell(String message) {
		this.message = message;
		this.hover = null;
	}

	public Cell(String message, String hover) {
		this.message = message;
		this.hover = hover;
	}

	public String getHover() {
		return hover;
	}

	public String getMessage() {
		return message;
	}

	public void setHover(String hover) {
		this.hover = hover;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
