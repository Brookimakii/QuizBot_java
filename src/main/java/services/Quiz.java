package services;

import exception.QuizEnded;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;
import model.Question;
import model.QuizQuestion;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.User;

/**
 * This is the Quiz class.
 */
public class Quiz {
  private final ArrayList<QuizQuestion> questions;
  private final ArrayList<Boolean> answer = new ArrayList<>();
  @Setter int actualQuestion;
  QuizQuestion currentQuestion;
  private Chronometer chronometer;
  
  @Getter @Setter private QuizSettings settings;
  @Getter @Setter private HashMap<QuizQuestion, HashMap<User, Integer>> score;
  
  private final int timeToChoose;
  private final int timeToNext;
  private final int timeToShow;
  
  private boolean isAnswerAvailable;
  
  /**
   * This methode set up the quiz.
   *
   * @param questions        the full list of question.
   * @param timeToChoose     the maximum amount of time to answer a question.
   * @param timeToNext       the time before the next question is asked.
   * @param timeToShow       the time before the choices appear.
   * @param numberOfQuestion the number of question.
   * @param numberOfChoices  the number of choices.
   */
  public Quiz(
      ArrayList<Question> questions, int timeToChoose, int timeToNext, int timeToShow,
      int numberOfQuestion, int numberOfChoices
  ) {
    this.questions = buildQuizQuestions(questions, numberOfQuestion, numberOfChoices);
    this.timeToChoose = timeToChoose;
    this.timeToNext = timeToNext;
    this.timeToShow = timeToShow;
  }
  
  private ArrayList<QuizQuestion> buildQuizQuestions(
      ArrayList<Question> questions, int numberOfQuestion, int numberOfChoices
  ) {
    ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    Collections.shuffle(questions);
    questions.subList(numberOfQuestion, questions.size()).clear();
    questions.forEach(question -> quizQuestions.add(new QuizQuestion(question, numberOfChoices)));
    return quizQuestions;
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * This method is the entry point of the quiz.
   *
   * @throws InterruptedException thrown when Time sleep or input failed.
   */
  public void startQuiz() throws InterruptedException {
    actualQuestion = 0;
    try {
      askNextQuestion();
    } catch (QuizEnded e) {
      result();
    }
  }
  
  private void askNextQuestion() throws InterruptedException, QuizEnded {
    ++actualQuestion;
    
    if (actualQuestion > questions.size()) {
      throw new QuizEnded();
    }
    System.out.println("─────────────────────────────────────────────────────────────────────────");
    
    this.currentQuestion = questions.get(actualQuestion - 1);
    System.out.println("Question n°" + actualQuestion + ": " + this.currentQuestion.getStatement());
    Thread.sleep(Duration.ofSeconds(timeToShow));
    
    this.currentQuestion.getChoices().forEach(choice -> System.out.println(
        (this.currentQuestion.getChoices().indexOf(choice) + 1) + "_ " + choice));
    
    chronometer = new Chronometer(timeToChoose, this, "questionTimeout");
    
    readAnswer();
  }
  
  //------------------------------------------------------------------------------------------------
  
  private void readAnswer() throws QuizEnded, InterruptedException {
    this.isAnswerAvailable = true;
    Scanner scanner = new Scanner(System.in);
    int answer = getUserAnswer(this.currentQuestion.getChoices().size(), scanner);
    long timeTaken = chronometer.stop();
    if (!isAnswerAvailable) {
      return;
    }
    if (answer == this.currentQuestion.getAnswerId() + 1) {
      this.answer.add(true);
      showAnswer(1);
    } else {
      this.answer.add(false);
      showAnswer(0);
    }
  }
  
  private int getUserAnswer(int numberOfChoices, Scanner scanner) {
    String answer;
    while (true) {
      answer = scanner.next();
      try {
        int answerInt = Integer.parseInt(answer);
        if (answerInt > 0 && answerInt <= numberOfChoices) {
          return answerInt;
        }
        System.out.println("Veuillez entrer un nombre valide");
      } catch (NumberFormatException e) {
        System.out.println("Veuillez entrer un nombre");
      }
    }
    
  }
  
  
  /**
   * This methode is responsible for chronometer timeout.
   *
   * @throws QuizEnded            thrown when there isn't more question to answer.
   * @throws InterruptedException thrown when Time sleep or input failed.
   */
  public void questionTimeout() throws QuizEnded, InterruptedException {
    this.isAnswerAvailable = false;
    this.answer.add(false);
    showAnswer(-1);
  }
  
  //------------------------------------------------------------------------------------------------
  
  private void showAnswer(int answer) throws QuizEnded, InterruptedException {
    switch (answer) {
      case -1 -> System.out.println("Timeout");
      case 0 -> System.out.println("Incorrect");
      case 1 -> System.out.println("Correct");
      default -> System.out.println("Should be Unreachable Statement");
    }
    int answerId = this.currentQuestion.getAnswerId();
    String answerString = this.currentQuestion.getChoices().get(answerId);
    System.out.println("The correct answer is: n°" + (answerId + 1) + ": " + answerString);
    if (timeToNext == 0) {
      Scanner scanner = new Scanner(System.in);
      String input = "";
      while (!input.equals("n")) {
        System.out.println("Press 'n' to continue or 'r' to report the question");
        input = scanner.next();
        switch (input) {
          case "n" -> askNextQuestion();
          case "r" -> GitHubReport.prepareQuestionReport(this.currentQuestion.getId());
          default -> System.out.println("Veuillez entrer un caractère valide");
        }
      }
    } else {
      Thread.sleep(Duration.ofSeconds(timeToNext));
      askNextQuestion();
    }
  }
  
  private void result() {
    System.out.println(
        "Your score: " + answer.stream().filter(bool -> bool = true).toList().size() + "/"
        + questions.size());
  }
  
  //------------------------------------------------------------------------------------------------
  
}
