package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

import me.bot.base.Bot;

public class Confirmation extends Bool {
    
    public Confirmation(Bot bot, User user, MessageChannel channel, String question, String tail, boolean skipable, long inactiveTime) {
        
        super(bot, user, channel, question, tail, skipable, inactiveTime);
        
    }
    
    
    private static final String TRUE_REGEX  = "1|yes|true|y|confirm";
    private static final String FALSE_REGEX = "0|no|false|n|cancel";
    
    @Override
    public boolean onTrigger(Message message) {
        
        String content = message.getContent().orElse("");
        String lcase = content.toLowerCase();
        
        if(!lcase.matches(TRUE_REGEX) && !lcase.matches(FALSE_REGEX)) {
            return false;
        }
        
        onEnd(lcase.matches(TRUE_REGEX));
        message.delete().subscribe();
        return true;
    }
}
