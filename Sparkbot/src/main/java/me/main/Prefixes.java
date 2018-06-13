package me.main;


import discord4j.core.object.entity.Guild;

public class Prefixes {

    private static final String
            NORMAL_PREFIX = "s!",
            ADMIN_PREFIX = "s&",
            SUPERADMIN_PREFIX = "s$";

    public static String[] getNormalPrefixesFor(Guild guild) {

        String sprefix = getNormalPrefixFor(guild);
        String prefix = getNormalPrefix();
        String[] out;
        if(sprefix.equalsIgnoreCase(prefix)) {
            out = new String[1];
            out[0] = prefix;
        } else {
            out = new String[2];
            out[0] = prefix;
            out[1] = sprefix;
        }
        return out;
    }

    public static String[] getAdminPrefixesFor(Guild guild) {

        String sprefix = getAdminPrefixFor(guild);
        String prefix = getAdminPrefix();
        String[] out;
        if(sprefix.equalsIgnoreCase(prefix)) {
            out = new String[1];
            out[0] = prefix;
        } else {
            out = new String[2];
            out[0] = prefix;
            out[1] = sprefix;
        }
        return out;
    }

    public static String getNormalPrefixFor(Guild guild) {
        return getNormalPrefix();
    }

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
        return SUPERADMIN_PREFIX;
    }

}
