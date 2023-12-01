package me.bot.base.polls;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class Confirmation extends Bool {
    
    public Confirmation(User user, MessageChannel channel, String question, String tail, boolean skippable, long inactiveTime) {
        
        super(user, channel, question, tail, skippable, inactiveTime);
        
    }
    
    
    private static final String TRUE_REGEX  = "1|yes|true|y|confirm";
    private static final String FALSE_REGEX = "0|no|false|n|cancel";
    
    @Override
    public boolean onTrigger(Message message) {
        
        String content = message.getContent();
        String lowerCase = content.toLowerCase();
        
        if(!lowerCase.matches(TRUE_REGEX) && !lowerCase.matches(FALSE_REGEX)) {
            return false;
        }
        
        onEnd(lowerCase.matches(TRUE_REGEX));
        message.delete().subscribe();
        return true;
    }
}
