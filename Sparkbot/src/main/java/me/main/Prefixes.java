package me.main;

import sx.blah.discord.handle.obj.IGuild;

public class Prefixes {

    private static final String
            NORMAL_PREFIX = "s!",
            ADMIN_PREFIX = "s&",
            SUPERADMIN_PREFIX = "s$";

    public static String[] getNormalPrefixesFor(IGuild guild) {

        String sprefix = getNormalPrefixFor(guild);
        String prefix = getNormalPrefix();
        String[] out;
        if(sprefix == prefix) {
            out = new String[1];
            out[0] = prefix;
        } else {
            out = new String[2];
            out[0] = prefix;
            out[1] = sprefix;
        }
        return out;
    }

    public static String[] getAdminPrefixesFor(IGuild guild) {

        String sprefix = getAdminPrefixFor(guild);
        String prefix = getAdminPrefix();
        String[] out;
        if(sprefix == prefix) {
            out = new String[1];
            out[0] = prefix;
        } else {
            out = new String[2];
            out[0] = prefix;
            out[1] = sprefix;
        }
        return out;
    }

    public static String getNormalPrefixFor(IGuild guild) {
        return getNormalPrefix();
    }

    public static String getAdminPrefixFor(IGuild guild) {
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
