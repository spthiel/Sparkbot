package me.main;

import me.bot.base.Bot;
import me.bot.commands.admin.Delete;
import me.bot.commands.user.Invite;
import me.bot.commands.user.Ping;

public class Main {
    
    private static Bot bot;
    
    public static void main(String[] args) {
        
        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

        bot.addCommands(new Ping(),new Delete(),new Invite());

    }
    
    public static Bot getBot() {
        return bot;
    }
    
}
