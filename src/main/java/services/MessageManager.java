package services;

import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class MessageManager {
  public static void sendMessage(MessageChannel channel, String msg) {
    channel.sendMessage(msg).queue();
  }
  
  public static void sendTemporaryMessage(MessageChannel channel, String msg, int delay) {
    channel.sendMessage(msg).queue(message -> message.delete().queueAfter(delay, TimeUnit.SECONDS));
  }
  
  public static void modifyMessage(Message message, String msg) {
    message.editMessage(msg).queue();
  }
  
  public static void temporaryModifyMessage(Message message, String msg, int delay) {
    String oldMsg = message.getContentRaw();
    message.editMessage(msg).queue();
    message.editMessage(oldMsg).queueAfter(delay,TimeUnit.SECONDS);
  }
  
  public static void deleteMessage(Message message, String msg) {
    message.delete().queue();
  }
}
