package repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.QuizQuestion;
import model.QuizQuestionStored;
import net.dv8tion.jda.api.entities.User;

@ToString
public class QuizSave {
  @Getter @Setter private int id;
  @Getter @Setter private ArrayList<QuizQuestionStored> score;
  @Getter @Setter private HashMap<String, Integer> playerScore;
  @Getter @Setter private int maxScore;
  
  
  public QuizSave() {
  }
  
  public QuizSave(
      HashMap<QuizQuestion, HashMap<User, Integer>> score, HashMap<User, Integer> playerScore,
      int maxScore
  ) throws IOException {
    this.id = Resources.appendQuizId();
    this.score = new ArrayList<>();
    score.forEach((question, userScore) -> {
      HashMap<String, Integer> userScoreString = new HashMap<>();
      userScore.forEach((user, answer) -> userScoreString.put(user.getName(), answer));
      this.score.add(new QuizQuestionStored(question, userScoreString));
    });
    this.playerScore = new HashMap<>();
    playerScore.forEach((user, scoreInt) -> this.playerScore.put(user.getName(), scoreInt));
    this.maxScore = maxScore;
    Resources.saveQuiz(this);
  }
}
