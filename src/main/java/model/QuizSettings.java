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
  
  public QuizSettings(User roomMaster) {
    this.roomMaster = roomMaster;
    this.players = new ArrayList<>();
    this.players.add(roomMaster);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void addPlayer(User user) {
    this.players.add(user);
  }
  
  public void removePlayer(User playerToRemove) {
    this.players.remove(playerToRemove);
  }
  
  public void addPlayer(String value) {
    User newPlayer = Bot.getJda().getUserById(value);
    System.out.println("New player: " + newPlayer);
    Member addedUser =
        this.getQuizThread().getParentChannel().getGuild().getMemberById(value);
    System.out.println("Added user: " + addedUser);
    if (addedUser == null) {
      this.getQuizThread().sendMessage("Unable to find the user: " + value)
          .queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
      return;
    }
    addPlayer(addedUser.getUser());
  }
  
  public void addPlayer(List<Member> members) {
    for (Member member : members) {
      addPlayer(member.getUser());
    }
  }
  
  public void removePlayer(String userName) {
    User playerToRemove =
        this.players.stream().filter(user -> userName.contains(user.getId())).findFirst()
            .orElse(null);
    System.out.println(playerToRemove);
    removePlayer(playerToRemove);
  }
  
  public void removePlayer(List<Member> members) {
    for (Member member : members) {
      removePlayer(member.getUser());
    }
  }
  
  
  public void start() {
    this.isRunning = true;
  }
  
  public void backup() {
    this.isRunning = true;
  }
  
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
