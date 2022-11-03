package services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import model.Question;
import model.QuizQuestion;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import repositories.Resources;

public class DiscordQuiz {
  private final ArrayList<QuizQuestion> questions = new ArrayList<>();
  @Setter int actualQuestion;
  QuizQuestion currentQuestion;
  private Chronometer chronometer;
  
  List<String> emotes =
      List.of("\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA",
          "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF",
          "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4",
          "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9"
      );
  
  @Getter @Setter private QuizSettings setting;
  @Getter @Setter private HashMap<QuizQuestion, HashMap<User, Integer>> score;
  private final HashMap<User, Integer> playerScoreTemplate;
  
  private boolean isAnswerAvailable = false;
  
  public DiscordQuiz(QuizSettings setting, Message msg) {
    setting.setQuestionMessage(msg);
    
    ArrayList<Question> listOfQuestion = Resources.getQuestions();
    Collections.shuffle(listOfQuestion);
    listOfQuestion.subList(setting.getQuestionNumber(), listOfQuestion.size()).clear();
    listOfQuestion.forEach(
        question -> this.questions.add(new QuizQuestion(question, setting.getChoicesNumber())));
    playerScoreTemplate = new HashMap<>();
    score = new HashMap<>();
    setting.getPlayers().forEach(player -> playerScoreTemplate.put(player, null));
    questions.forEach(quizQuestion -> score.put(quizQuestion, playerScoreTemplate));
    
    setting.setQuiz(this);
    this.setting = setting;
  }
  
  
  public void nextQuestion() {
    ++actualQuestion;
    this.currentQuestion = questions.get(actualQuestion - 1);
    askQuestion();
  }
  
  private void askQuestion() {
    isAnswerAvailable = false;
    setting.getQuestionMessage().clearReactions().complete();
    List<String> answerEmote = emotes.subList(0, currentQuestion.getChoices().size());
    System.out.println("Number of choices: "+currentQuestion.getChoices().size()+": "+answerEmote);
    
    setting.getQuestionMessage().editMessage(
        "Question n°" + actualQuestion + ": " + this.currentQuestion.getStatement()
    ).queue(message -> answerEmote.forEach(
        reaction -> message.addReaction(Emoji.fromUnicode(reaction)).queue()));
    try {
      Thread.sleep(Duration.ofSeconds(setting.getTimeToShowChoices()));
    } catch (InterruptedException e) {
      System.out.println("Error while waiting for the choices to appear");
      throw new RuntimeException(e);
    }
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < currentQuestion.getChoices().size(); ++i) {
      sb.append(emotes.get(i)).append(" - ").append(currentQuestion.getChoices().get(i))
          .append("\n");
    }
    
    setting.getQuestionMessage().editMessage(
            "Question n°" + actualQuestion + ": " + this.currentQuestion.getStatement() + "\n" + sb)
        .queue();
    
    this.isAnswerAvailable = true;
    this.chronometer = new Chronometer(setting.getTimeToAnswer(), this, "discordTimeout");
  }
  
  public void registerAnswer(User player, int answer) {
    score.get(currentQuestion).replace(player, answer);
    int count = Collections.frequency(score.get(currentQuestion).values(), null);
    if (count == 0) {
      System.out.println("End");
    }
  }
  
  public void discordTimeout() {
    System.out.println("Timeout");
  }
}
