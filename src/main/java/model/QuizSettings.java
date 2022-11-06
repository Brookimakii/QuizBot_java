package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import services.DiscordQuiz;

/**
 * This class contains all the settings for a quiz.
 */
public class QuizSettings {
  @Getter @Setter private DiscordQuiz quiz;
  
  @Getter private final User roomMaster;
  @Getter @Setter private Message parameterMessage;
  @Getter @Setter private Message originalMessage;
  @Getter @Setter private Message questionMessage;
  @Getter @Setter private ThreadChannel quizThread;
  @Getter private final ArrayList<User> players;
  
  @Getter @Setter private String title;
  
  @Getter @Setter private int timeToShowChoices = 5;
  @Getter @Setter private int timeToPassToNext = 5;
  @Getter @Setter private int timeToAnswer = 30;
  
  @Getter @Setter private int questionNumber = 5;
  @Getter @Setter private int choicesNumber = 4;
  
  @Getter @Setter private boolean isRunning = false;
  
  @Override
  public String toString() {
    return "QuizSettings{" + "quiz=" + quiz + ", roomMaster=" + roomMaster + ", parameterMessage="
           + parameterMessage + ", originalMessage=" + originalMessage + ", questionMessage="
           + questionMessage + ", quizThread=" + quizThread + ", players=" + players + ", title='"
           + title + '\'' + ", timeToShowChoices=" + timeToShowChoices + ", timeToPassToNext="
           + timeToPassToNext + ", timeToAnswer=" + timeToAnswer + ", questionNumber="
           + questionNumber + ", choicesNumber=" + choicesNumber + ", isRunning=" + isRunning + '}';
  }
  
  /**
   * This constructor is used to create a new quiz.
   *
   * @param roomMaster The user who created the quiz.
   */
  public QuizSettings(User roomMaster) {
    this.roomMaster = roomMaster;
    this.players = new ArrayList<>();
    this.players.add(roomMaster);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * The method to add a player to the quiz.
   *
   * @param user the user to add.
   */
  public void addPlayer(User user) {
    this.players.add(user);
  }
  
  /**
   * This method is used to add a player from the list of players.
   *
   * @param members The list of members to add.
   */
  public void addPlayer(List<Member> members) {
    for (Member member : members) {
      addPlayer(member.getUser());
    }
  }
  
  /**
   * This method is used to add a player from the quiz.
   *
   * @param value the name of the user to remove.
   */
  public void addPlayer(String value) {
    User newPlayer = Bot.getJda().getUserById(value);
    System.out.println("New player: " + newPlayer);
    Member addedUser = this.getQuizThread().getParentChannel().getGuild().getMemberById(value);
    System.out.println("Added user: " + addedUser);
    if (addedUser == null) {
      this.getQuizThread().sendMessage("Unable to find the user: " + value)
          .queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
      return;
    }
    addPlayer(addedUser.getUser());
  }
  
  
  /**
   * This method is used to remove a player from the list of players.
   *
   * @param user The user to remove.
   */
  public void removePlayer(User user) {
    this.players.remove(user);
  }
  
  /**
   * This method is used to remove all the players from the list.
   *
   * @param members The list of members to remove.
   */
  public void removePlayer(List<Member> members) {
    for (Member member : members) {
      removePlayer(member.getUser());
    }
  }
  
  /**
   * This method is used to remove a player from the quiz.
   *
   * @param userName the name of the user to remove
   */
  public void removePlayer(String userName) {
    User playerToRemove =
        this.players.stream().filter(user -> userName.contains(user.getId())).findFirst()
            .orElse(null);
    System.out.println(playerToRemove);
    removePlayer(playerToRemove);
  }
  

  
  
  public void start() {
    this.isRunning = true;
  }
  
  public void backup() {
    this.isRunning = true;
  }
  
  /**
   * This print the settings of the quiz.
   *
   * @return the message to send.
   */
  public String toStyleString() {
    StringBuilder builder = new StringBuilder();
    players.forEach(player -> builder.append("> <@").append(player.getId()).append(">\n"));
    
    return "" + "Title: " + title + "\n" + "Room Master: " + roomMaster.getName() + "\n\n"
           + "Number of questions: " + questionNumber + "\n" + "Number of choices: " + choicesNumber
           + "\n\n" + "Time to choose an answer: " + timeToAnswer + "\n"
           + "Time to before the answers are shown: " + timeToShowChoices + "\n"
           + "Time to pass to the next question: " + timeToPassToNext + "\n\n" + "Players:\n"
           + builder + "\n";
  }
}
