package model;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Question {
  @Getter @Setter private long id;
  @Getter @Setter private String statement;
  @Getter @Setter private String answer;
  @Getter @Setter private ArrayList<String> choices;
  
  public Question(long id, String statement, String answer, ArrayList<String> choices) {
    this.id = id;
    this.statement = statement;
    this.answer = answer;
    this.choices = choices;
  }
}
