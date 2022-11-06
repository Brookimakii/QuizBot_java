package controller;

import java.io.IOException;
import java.util.ArrayList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import repositories.PropertiesManager;
import services.EmbedMessageBuilder;

/**
 * This class is used to listen to events from the bot.
 */
public class BotEventListener extends ListenerAdapter {
  /**
   * This method is used to handle the event when a message, a reaction or an embed button press is
   * received.
   *
   * @param event The event that is received.
   */
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }
    User user = event.getAuthor();
    Message message = event.getMessage();
    String messageId = message.getId();
    
    String content = message.getContentRaw();
    MessageChannel channel = event.getChannel();
    Guild guild = event.getGuild();
    
    
    System.out.println("We received a message from " + user.getName() + ": " + content);
    String prefix = PropertiesManager.getProperty("prefix");
    if (content.startsWith(prefix)) {
      String command = content.substring(prefix.length());
      try {
        CommandManager.command(command, event);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
  }
  
  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    if (event.getUser() == null) {
      return;
    }
    if (event.getUser().isBot()) {
      return;
    }
    
    User user = event.getUser();
    MessageReaction reaction = event.getReaction();
    System.out.println(
        "We received a reaction from " + user.getName() + ": " + reaction.getEmoji().getName());
    EmoteManager.emote(event);
  }
  
  @Override
  public void onButtonInteraction(ButtonInteractionEvent event) {
    String buttonId = event.getButton().getId();
    if (buttonId == null || buttonId.equals("")) {
      return;
    }
    String[] args = buttonId.split("_");
    String name = event.getButton().getId();
    System.out.println("Button pressed: " + name);
    if (args[1].equalsIgnoreCase("page")) {
      if (args[2].equalsIgnoreCase("cancel")) {
        event.getMessage().delete().queue();
        return;
      }
      int pageNumber = Integer.parseInt(args[2]);
      EmbedMessageBuilder builder = new EmbedMessageBuilder();
      if (args[0].equals("help")) {
        builder.getHelpEmbed(pageNumber);
      }
      event.getMessage().editMessageEmbeds(builder.getMessageEmbed())
          .setActionRow(builder.getButtons()).queue();
    }
  }
  
}
