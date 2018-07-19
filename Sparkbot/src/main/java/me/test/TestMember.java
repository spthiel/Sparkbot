package me.test;

public class TestMember {

	private String name,displayname;
	private int discrim;
	
	public TestMember(String name, int discrim) {
		this.name = name;
		this.displayname = name;
		this.discrim = discrim;
	}
	
	public TestMember(String name, String nick, int discrim) {
		this.name = name;
		this.displayname = nick;
		this.discrim = discrim;
	}
	
	public String getUsername() {
		
		return name;
	}
	
	public String getDisplayName() {
		
		return displayname;
	}
	
	public int getDiscriminator() {
		
		return discrim;
	}
}
