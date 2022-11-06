package services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import lombok.Getter;
import lombok.Setter;
import model.Bot;
import model.Question;
import model.QuizQuestion;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.NotNull;
import repositories.PropertiesManager;
import repositories.Resources;

/**
 * The discord version of the quiz.
 */
public class DiscordQuiz {
  private final ArrayList<QuizQuestion> questions = new ArrayList<>();
  @Setter int actualQuestion;
  @Getter QuizQuestion currentQuestion;
  private Chronometer chronometer;
  
  @Getter private final int timeToShowChoices;
  @Getter private final int timeToPassToNext;
  @Getter private final int timeToAnswer;
  
  @Getter List<String> emotes =
      List.of("\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA",
          "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF",
          "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4",
          "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9"
      );
  
  @Getter @Setter private QuizSettings setting;
  @Getter @Setter private LinkedHashMap<QuizQuestion, HashMap<User, Integer>> score;
  private final HashMap<User, Integer> playerScoreTemplate;
  
  @Getter private boolean isAnswerAvailable = false;
  @Getter private boolean isPassingNeeded = false;
  
  /**
   * Set up the quiz.
   *
   * @param setting The quiz settings.
   * @param msg     The message that triggered the quiz.
   */
  public DiscordQuiz(QuizSettings setting, Message msg) {
    this.setting = setting;
    this.timeToShowChoices = setting.getTimeToShowChoices();
    this.timeToPassToNext = setting.getTimeToPassToNext();
    this.timeToAnswer = setting.getTimeToAnswer();
    setting.setQuestionMessage(msg);
    
    ArrayList<Question> listOfQuestion = Resources.getQuestions();
    Collections.shuffle(listOfQuestion);
    listOfQuestion.subList(setting.getQuestionNumber(), listOfQuestion.size()).clear();
    listOfQuestion.forEach(
        question -> this.questions.add(new QuizQuestion(question, setting.getChoicesNumber())));
    playerScoreTemplate = new HashMap<>();
    score = new LinkedHashMap<>() {};
    setting.getPlayers().forEach(player -> playerScoreTemplate.put(player, null));
    this.questions.forEach(
        quizQuestion -> score.put(quizQuestion, new HashMap<>(playerScoreTemplate)));
    setting.setQuiz(this);
  }
  
  /**
   * Set up the next question.
   */
  public void nextQuestion() {
    ++actualQuestion;
    if (actualQuestion > questions.size()) {
      calculateScore();
      printScore();
    } else {
      this.currentQuestion = questions.get(actualQuestion - 1);
      askQuestion();
    }
  }
  
  private void calculateScore() {
    int questionMaxScore = Integer.parseInt(PropertiesManager.getProperty("questionMaxScore"));
    int maxScore = 0;
    int maxStreak = 0;
    
    HashMap<User, Integer> playerScore = new HashMap<>();
    HashMap<User, Integer> playerStreak = new HashMap<>();
    setting.getPlayers().forEach(player -> {
      playerScore.put(player, 0);
      playerStreak.put(player, 0);
    });
    
    for (Map.Entry<QuizQuestion, HashMap<User, Integer>> question : score.entrySet()) {
      QuizQuestion quizQuestion = question.getKey();
      maxStreak++;
      maxScore += questionMaxScore + streakBonus(maxStreak);
      
      for (Map.Entry<User, Integer> playerAnswer : question.getValue().entrySet()) {
        User player = playerAnswer.getKey();
        int answer = playerAnswer.getValue();
        int userScore = playerScore.get(player);
        if (answer == quizQuestion.getAnswerId()) {
          playerStreak.put(player, playerStreak.get(player) + 1);
          userScore += questionMaxScore + streakBonus(playerStreak.get(player));
          playerScore.put(player, userScore);
        } else {
          playerStreak.put(player, 0);
        }
      }
    }
    
    System.out.println("Max Score" + ": " + score + "\n");
    
    playerScore.forEach((player, score) -> System.out.println(player.getName() + " <==> " + score));
  }
  
  private int streakBonus(int streak) {
    int questionMaxScore = Integer.parseInt(PropertiesManager.getProperty("questionMaxScore"));
    if (streak > 1 && streak <= 5) {
      return (questionMaxScore / 100) * (streak - 1);
    } else if (streak > 5) {
      return (questionMaxScore / 100) * 5;
    }
    return 0;
  }
  
  
  private void askQuestion() {
    isAnswerAvailable = false;
    setting.getQuestionMessage().clearReactions().complete();
    List<String> answerEmote = emotes.subList(0, currentQuestion.getChoices().size());
    
    setting.getQuestionMessage().editMessage(statementBuild()).queue(message -> answerEmote.forEach(
        reaction -> message.addReaction(Emoji.fromUnicode(reaction)).queue()));
    try {
      Thread.sleep(Duration.ofSeconds(setting.getTimeToShowChoices()));
    } catch (InterruptedException e) {
      System.out.println("Error while waiting for the choices to appear");
      throw new RuntimeException(e);
    }
    
    setting.getQuestionMessage().editMessage(statementBuild() + "\n" + choicesBuild()).queue();
    
    this.isAnswerAvailable = true;
    this.chronometer = new Chronometer(setting.getTimeToAnswer(), this, "discordTimeout");
  }
  
  /**
   * Register the answer of a player.
   *
   * @param player the player who answered.
   * @param answer the answer of the player.
   */
  public void registerAnswer(User player, int answer) {
    if (score.get(currentQuestion).get(player) == null) {
      score.get(currentQuestion).replace(player, answer);
    }
    int count = Collections.frequency(score.get(currentQuestion).values(), null);
    if (count == 0) {
      System.out.println("End");
      chronometer.stop();
      this.isAnswerAvailable = false;
      showAnswer();
    }
  }
  
  /**
   * Show the scores.
   */
  public void printScore() {
    score.forEach((question, userIntegerHashMap) -> {
      System.out.println(question);
      userIntegerHashMap.forEach(
          (user, integer) -> System.out.println("\t" + user.getName() + " <=> " + integer));
    });
  }
  
  /**
   * The Question timeout event.
   */
  public void discordTimeout() {
    System.out.println("Timeout");
    this.isAnswerAvailable = false;
    showAnswer();
  }
  
  private void showAnswer() {
    System.out.println("Show answer");
    
    setting.getQuestionMessage().editMessage(statementBuild() + "\n" + answerBuild()).queue();
    if (setting.getTimeToPassToNext() > 0) {
      try {
        Thread.sleep(Duration.ofSeconds(setting.getTimeToPassToNext()));
      } catch (InterruptedException e) {
        System.out.println("Error while waiting for the choices to appear");
        throw new RuntimeException(e);
      }
    } else {
      System.out.println("No time to pass to next question");
      setting.getQuestionMessage().clearReactions().complete();
      setting.getQuestionMessage().editMessage(
              statementBuild() + "\n" + answerBuild() + "--------------------------------\n\n"
              + "\u23ED\uFE0F - Next question.\n" + "\u2757 - Report question.")
          .queue(message -> setting.setQuestionMessage(message));
      setting.getQuestionMessage().addReaction(Emoji.fromUnicode("\u23ED\uFE0F")).queue();
      setting.getQuestionMessage().addReaction(Emoji.fromUnicode("\u2757")).queue();
      
      this.isPassingNeeded = true;
    }
  }
  
  @NotNull
  private String statementBuild() {
    return "Question n°" + actualQuestion + ": " + this.currentQuestion.getStatement();
  }
  
  @NotNull
  private String choicesBuild() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < currentQuestion.getChoices().size(); ++i) {
      sb.append(emotes.get(i)).append(" - ").append(currentQuestion.getChoices().get(i))
          .append("\n");
    }
    return sb.toString();
  }
  
  @NotNull
  private String answerBuild() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < currentQuestion.getChoices().size(); ++i) {
      if (i == currentQuestion.getAnswerId()) {
        sb.append(":white_check_mark:");
      } else {
        sb.append(":x:");
      }
      sb.append(" - ").append(currentQuestion.getChoices().get(i)).append("\n");
    }
    return sb.toString();
  }
  
  
}
