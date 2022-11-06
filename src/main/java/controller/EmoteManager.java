package controller;

import java.util.List;
import java.util.stream.Collectors;
import model.Bot;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import repositories.PropertiesManager;
import repositories.Resources;

/**
 * This class is used to manage the emotes.
 */
public class EmoteManager {
  /**
   * This method is used to manage the emotes.
   *
   * @param event The event.
   */
  public static void emote(MessageReactionAddEvent event) {
    QuizSettings settings = Resources.getFilteredSetting(event.getChannel());
    User user = event.getUser();
    
    if (settings == null || user == null) {
      return;
    }
    if (!settings.getQuestionMessage().getId().equals(event.getMessageId())) {
      event.getReaction().removeReaction(user).queue();
      return;
    }
    if (!settings.getPlayers().contains(user)) {
      return;
    }
    
    String emote = event.getReaction().getEmoji().getName();
    List<String> emotes = settings.getQuiz().getEmotes();
    int answer = emotes.indexOf(emote);
    
    if (settings.getQuiz().isAnswerAvailable()) {
      if (answer > settings.getQuiz().getCurrentQuestion().getChoices().size()) {
        event.getReaction().removeReaction(user).queue();
      } else {
        event.getReaction().removeReaction(user).queue();
        Integer storedAnswer =
            settings.getQuiz().getScore().get(settings.getQuiz().getCurrentQuestion()).get(user);
        if (storedAnswer == null) {
          System.out.println("User " + user.getName() + " answered " + answer);
        } else {
          System.out.println(
              "User " + user.getName() + " has already answered " + storedAnswer + " cannot answer "
              + answer);
        }
        settings.getQuiz().registerAnswer(user, answer);
      }
    } else if (settings.getQuiz().isPassingNeeded()) {
      Message message =
          event.getChannel().retrieveMessageById(settings.getQuestionMessage().getId()).complete();
      for (MessageReaction reaction : message.getReactions()) {
        System.out.println(reaction.getEmoji().getName());
      }
      String emoteNext = PropertiesManager.getProperty("emoteNext");
      String emoteReport = PropertiesManager.getProperty("emoteReport");
      if (emote.equals(emoteNext)) {
        if (user.equals(settings.getRoomMaster())) {
          settings.getQuiz().nextQuestion();
        } else {
          int numberOfCheck = settings.getQuestionMessage().getReactions().stream()
              .filter(reaction -> reaction.getEmoji().equals(Emoji.fromUnicode("✅"))).toList()
              .size();
          if (100 * numberOfCheck / settings.getPlayers().size() >= 75) {
            settings.getQuiz().nextQuestion();
          }
        }
      } else if (emote.equals(emoteReport)) {
        System.out.println("Reporting");
        event.getReaction().removeReaction(user).queue();
      } else {
        event.getReaction().removeReaction(user).queue();
      }
    } else {
      event.getReaction().removeReaction(user).queue();
    }
    
  }
}
