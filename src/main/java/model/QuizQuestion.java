package model;

import exception.InvalidNumberOfChoices;
import java.util.ArrayList;
import java.util.Collections;
import lombok.Getter;
import lombok.ToString;

/**
 * The question quiz class.
 */
@ToString
public class QuizQuestion {
  @Getter private final long id;
  @Getter private final String statement;
  @Getter private final int answerId;
  @Getter private final ArrayList<String> choices;
  
  
  
  /**
   * The question quizz Constructor.
   *
   * @param question        the question to build.
   * @param numberOfChoices the number of choices.
   */
  public QuizQuestion(Question question, int numberOfChoices) {
    if (numberOfChoices < 2) {
      throw new InvalidNumberOfChoices();
    }
    --numberOfChoices;
    this.id = question.getId();
    this.statement = question.getStatement();
  
    ArrayList<String> choices = buildChoices(question, numberOfChoices);
    this.choices = choices;
    this.answerId = choices.indexOf(question.getAnswer());
  }
  
  private ArrayList<String> buildChoices(Question question, int numberOfChoices) {
    ArrayList<String> choices = new ArrayList<>(question.getChoices());
    Collections.shuffle(choices);
    if (numberOfChoices < choices.size()) {
      choices.subList(numberOfChoices, choices.size()).clear();
    }
    choices.add(question.getAnswer());
    Collections.shuffle(choices);
    return choices;
  }
}


