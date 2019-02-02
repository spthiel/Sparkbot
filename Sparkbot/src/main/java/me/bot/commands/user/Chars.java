package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.polls.Input;
import me.bot.base.polls.PollExitType;
import me.main.Prefixes;

import java.util.*;
import java.util.function.Consumer;

public class Chars implements ICommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.PUBLIC;
    }
    
    @Override
    public String getHelp() {
        
        return "Design or return a char";
    }
    
    @Override
    public String[] getNames() {
        
        return new String[]{"character", "char"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return Prefixes.getNormalPrefixesFor(guild);
    }
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return null;
    }
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return null;
    }
    
    @Override
    public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
        
        if (args.length >= 1) {
            switch (args[0]) {
                case "new":
                case "create":
                    createCharacter(bot, author, channel, guild);
                    break;
                case "delete":
                case "remove":
                    
                    break;
                case "list":
                    
                    break;
            }
        } else {
            //TODO
        }
    }
    
    @Override
    public void onLoad(final Bot bot) {
    
    }
    
    private int createCharacter(Bot bot, Member user, TextChannel channel, Guild guild) {
        
        Map<String, Object> object = getConfig(bot, guild.getId().asLong());
        if (object.containsKey("questions")) {
            ArrayList<Object>   questions = (ArrayList) object.get("questions");
            Map<String, Object> character = new HashMap<>();
            Map<String, Object> author    = new HashMap<>();
            author.put("name", user.getUsername());
            author.put("id", user.getId().asLong());
            
            character.put("author", user);
            
            Iterator<Object> questionIterator = questions.iterator();
            
            recursive(bot, user, channel, questionIterator, (answers) -> {
            
            });
            
            
        } else {
            return -2;
        }
        return -1;
    }
    
    private void recursive(Bot bot, Member user, TextChannel channel, Iterator<Object> questions, Consumer<List<String>> onComplete) {
        
        recursive(bot, user, channel, questions, onComplete, new ArrayList<>());
    }
    
    private void recursive(Bot bot, Member user, TextChannel channel, Iterator<Object> questions, Consumer<List<String>> onComplete, List<String> answers) {
        
        if (!questions.hasNext()) {
            onComplete.accept(answers);
            return;
        }
        HashMap<String, Object> q        = (HashMap<String, Object>) questions.next();
        String                  question = (String) q.get("q");
        boolean                 skipable = (boolean) q.get("s");
        
        Input input = new Input(bot, user, channel, question, "Use `exit` to leave the Menu", skipable, -1);
        bot.addPoll(input);
        input.subscribe((result, type) -> {
            if (type.equals(PollExitType.SUCCESS)) {
                answers.add(result);
            } else {
                answers.add(null);
            }
            recursive(bot, user, channel, questions, onComplete, answers);
        });
        
    }
    
    private Map<String, Object> getConfig(Bot bot, long guildid) {
        
        return bot.getResourceManager().getConfig("configs/" + guildid + "/rp", "settings.json");
    }
    
    private void write(Bot bot, long guildid, HashMap<String, Object> object) {
        
        bot.getResourceManager().writeConfig("configs/" + guildid + "/rp", "settings.json", object);
    }
}
