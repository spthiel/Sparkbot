package me.main;


import discord4j.core.object.entity.Guild;

public class Prefixes {
	
	private static String
		NORMAL_PREFIX     = "s!",
		ADMIN_PREFIX      = "s&",
		SUPER_ADMIN_PREFIX = "s$";
	
	static {
		if (Main.testInstance) {
			NORMAL_PREFIX = "s!!";
			ADMIN_PREFIX = "s&&";
			SUPER_ADMIN_PREFIX = "s$$";
		}
	}
	
	public static String[] getNormalPrefixesFor(Guild guild) {
		
		String prefix      = getNormalPrefix();
		String guildPrefix = getNormalPrefixFor(guild);
		
		return avoidDuplicatePrefixes(prefix, guildPrefix);
	}
	
	public static String[] getAdminPrefixesFor(Guild guild) {
		
		String prefix      = getAdminPrefix();
		String guildPrefix = getAdminPrefixFor(guild);
		
		return avoidDuplicatePrefixes(prefix, guildPrefix);
	}
	
	private static String[] avoidDuplicatePrefixes(String prefixA, String prefixB) {
		
		String[] out;
		if (prefixA.equalsIgnoreCase(prefixB)) {
			out = new String[1];
			out[0] = prefixA;
		} else {
			out = new String[2];
			out[0] = prefixA;
			out[1] = prefixB;
		}
		return out;
	}
	
	@SuppressWarnings("unused")
	public static String getNormalPrefixFor(Guild guild) {
		
		return getNormalPrefix();
	}
	
	@SuppressWarnings("unused")
	public static String getAdminPrefixFor(Guild guild) {
		
		return getAdminPrefix();
	}
	
	public static String getNormalPrefix() {
		
		return NORMAL_PREFIX;
	}
	
	public static String getAdminPrefix() {
		
		return ADMIN_PREFIX;
	}
	
	public static String getSuperAdminPrefix() {
		
		return SUPER_ADMIN_PREFIX;
	}
	
}
