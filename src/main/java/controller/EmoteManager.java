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
import repositories.Resources;

public class EmoteManager {
  public static void emote(MessageReactionAddEvent event) {
    QuizSettings settings = Resources.getFilteredSetting(event.getChannel());
    User user = event.getUser();
    String emote = event.getReaction().getEmoji().getName();
    
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
      Message message = event.getChannel().retrieveMessageById(settings.getQuestionMessage().getId()).complete();
      for (MessageReaction reaction : message.getReactions()){
        System.out.println(reaction.getEmoji().getName());
      }
      switch (emote) {
        case "\u23ED\uFE0F"-> {
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
        }
        case "\u2757"-> {
          System.out.println("Reporting");
          event.getReaction().removeReaction(user).queue();
        }
        default-> event.getReaction().removeReaction(user).queue();
      }
    } else {
      event.getReaction().removeReaction(user).queue();
    }
    
  }
}
