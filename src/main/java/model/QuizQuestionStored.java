package model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class QuizQuestionStored {
  @Getter @Setter private QuizQuestion quizQuestion;
  @Getter @Setter private HashMap<String, Integer> answer;
  
  public QuizQuestionStored() {
  }
  
  
  public QuizQuestionStored(QuizQuestion question, HashMap<String, Integer> userScoreString) {
    this.quizQuestion = question;
    this.answer = userScoreString;
  }
}
