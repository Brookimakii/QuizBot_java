package model;

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
    --numberOfChoices;
    this.id = question.getId();
    this.statement = question.getStatement();
    ArrayList<String> choices = question.getChoices();
    Collections.shuffle(choices);
    choices.subList(numberOfChoices, choices.size()).clear();
    choices.add(question.getAnswer());
    this.choices = choices;
    Collections.shuffle(choices);
    this.answerId = choices.indexOf(question.getAnswer());
  }
}


