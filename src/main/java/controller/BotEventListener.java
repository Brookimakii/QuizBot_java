package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repositories.PropertiesManager;
import repositories.Resources;
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
    event.deferEdit().queue();
    
    String buttonId = event.getButton().getId();
    if (buttonId == null || buttonId.equals("")) {
      return;
    }
    List<String> args = Arrays.stream(buttonId.split("_")).toList();
    String name = event.getButton().getId();
    System.out.println("Button pressed: " + name);
    if (args.contains("page")) {
      if (args.contains("cancel")) {
        event.getMessage().delete().queue();
        return;
      }
      EmbedMessageBuilder builder = new EmbedMessageBuilder();
      if (args.contains("help")) {
        int pageNumber = Integer.parseInt(args.get(2));
        builder.getHelpEmbed(pageNumber);
      } else if (args.contains("score")) {
        int pageNumber = Integer.parseInt(args.get(3));
        int quizId = Integer.parseInt(args.get(1));
        try {
          builder.getScoreEmbed(Resources.loadQuizNo(quizId), pageNumber);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      //event.editMessageEmbeds(builder.getMessageEmbed()).setActionRow(builder.getButtons())
      // .queue();
      event.getMessage().editMessageEmbeds(builder.getMessageEmbed())
          .setActionRow(builder.getButtons()).queue();
    }
  }
  
}
