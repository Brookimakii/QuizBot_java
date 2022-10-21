package model;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {
  
  @Test
  void getId() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
    
    //When
    Question question = new Question(id, statement, answer, choices);
    
    //Then
    assertThat(question.getId()).isEqualTo(id);
  }
  
  @Test
  void getStatement() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    //When
    Question question = new Question(id, statement, answer, choices);
  
    //Then
    assertThat(question.getStatement()).isEqualTo(statement);
  }
  
  @Test
  void getAnswer() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    //When
    Question question = new Question(id, statement, answer, choices);
  
    //Then
    assertThat(question.getAnswer()).isEqualTo(answer);
  }
  
  @Test
  void getChoices() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    //When
    Question question = new Question(id, statement, answer, choices);
  
    //Then
    assertThat(question.getChoices()).isEqualTo(choices);
  }
  
  @Test
  void setId() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
    
    long newId = 2;
  
    //When
    Question question = new Question(id, statement, answer, choices);
    question.setId(newId);
    
  
    //Then
    assertThat(question.getId()).isEqualTo(newId);
  }
  
  @Test
  void setStatement() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    String newtsatement = "Combien de frontière terrestre possède la France ?";
  
    //When
    Question question = new Question(id, statement, answer, choices);
    question.setStatement(newtsatement);
  
  
    //Then
    assertThat(question.getStatement()).isEqualTo(newtsatement);
  }
  
  @Test
  void setAnswer() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    String newAnswer = "7";
  
    //When
    Question question = new Question(id, statement, answer, choices);
    question.setAnswer(newAnswer);
  
  
    //Then
    assertThat(question.getAnswer()).isEqualTo(newAnswer);
  }
  
  @Test
  void setChoices() {
    //Given
    long id = 1;
    String statement = "Combien de frontière possède la France ?";
    String answer = "5";
    ArrayList<String> choices = new ArrayList<>(Arrays.asList("1", "10", "100"));
  
    ArrayList<String> newChoices = new ArrayList<>(Arrays.asList("4", "6", "3"));
  
    //When
    Question question = new Question(id, statement, answer, choices);
    question.setChoices(newChoices);
  
  
    //Then
    assertThat(question.getChoices()).isEqualTo(newChoices);
  }
}