package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents a question.
 */
@ToString
public class Question {
  @JsonIgnoreProperties
  @Getter @Setter private long id;
  @Getter @Setter private String statement;
  @Getter @Setter private String answer;
  @Getter @Setter private ArrayList<String> choices;
  
  /**
   * This constructor creates a question.
   *
   * @param id        The id of the question.
   * @param statement The statement of the question.
   * @param answer    The answer of the question.
   * @param choices   The choices of the question.
   */
  public Question(long id, String statement, String answer, ArrayList<String> choices) {
    this.id = id;
    this.statement = statement;
    this.answer = answer;
    this.choices = choices;
  }
}
