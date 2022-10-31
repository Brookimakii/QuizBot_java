package controller;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repositories.PropertiesManager;

public class BotEventListener extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    User user = event.getAuthor();
    Message message = event.getMessage();
    String messageID = message.getId();
    
    String content = message.getContentRaw();
    MessageChannel channel = event.getChannel();
    Guild guild = event.getGuild();
    
    
    System.out.println("We received a message from " + user.getName() + ": " + content);
    String prefix = PropertiesManager.getProperty("prefix");
    if (content.startsWith(prefix)) {
      String command = content.substring(prefix.length());
    }
    
  }
  
  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    if (event.getUser() == null) return;
    if (event.getUser().isBot()) return;
    
    User user = event.getUser();
    String message = event.getMessageId();
    MessageReaction reaction = event.getReaction();
  }
}
