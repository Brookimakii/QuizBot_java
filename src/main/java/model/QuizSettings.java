package model;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;

public class QuizSettings {
  @Getter private final User roomMaster;
  @Getter @Setter private Message parameterMessage;
  @Getter @Setter private Message originalMessage;
  @Getter @Setter private ThreadChannel quizThread;
  @Getter private final ArrayList<User> players;
  
  @Getter @Setter private String title;
  
  @Getter @Setter private int timeToShowChoices = 5;
  @Getter @Setter private int timeToPassToNext = 5;
  @Getter @Setter private int timeToAnswer = 30;
  
  @Getter @Setter private int questionNumber = 5;
  @Getter @Setter private int choicesNumber = 4;
  
  @Getter @Setter private boolean isRunning = false;
  
  public QuizSettings(User roomMaster) {
    this.roomMaster = roomMaster;
    this.players = new ArrayList<>();
    this.players.add(roomMaster);
  }
  
  public void addPlayer(User user) {
    this.players.add(user);
  }
  
  public void removePlayer(User playerToRemove) {
    this.players.remove(playerToRemove);
  }
  
  public void addPlayer(String value) {
    Member addedUser =
        this.getQuizThread().getParentChannel().getGuild().getMemberById(value);
    if (addedUser == null) {
      this.getQuizThread().sendMessage("Unable to find the user: " + value)
          .queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
      return;
    }
    addPlayer(addedUser.getUser());
  }
  
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
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    players.forEach(player -> builder.append("> ").append(player.getName()));
    
    return "" + "Title: " + title + "\n" + "Room Master: " + roomMaster.getName() + "\n\n"
           + "Number of questions: " + questionNumber + "\n" + "Number of choices: " + choicesNumber
           + "\n\n" + "Time to choose an answer: " + timeToAnswer + "\n"
           + "Time to before the answers are shown: " + timeToShowChoices + "\n"
           + "Time to pass to the next question: " + timeToPassToNext + "\n\n" + "Players:\n"
           + builder + "\n";
  }
}
