import exception.NoFileFound;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import model.Question;
import model.QuizQuestion;
import services.PersistanceDriver;

/**
 * This class is the entry point of the program.
 */
public class Main {
  /**
   * Main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    try {
      startQuiz();
    } catch (NoFileFound | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void startQuiz() throws NoFileFound, IOException {
    ArrayList<Question> questions = PersistanceDriver.loadQuestion();
    
    Collections.shuffle(questions);
    int numberOfQuestions = 10;
    int numberOfChoices = 4;
    questions.subList(numberOfQuestions, questions.size()).clear();
    ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    questions.forEach(question -> quizQuestions.add(new QuizQuestion(question, numberOfChoices)));
    int score = 0;
    for (QuizQuestion question : quizQuestions) {
      System.out.println(question.getStatement());
      question.getChoices().forEach(
          choice -> System.out.println(question.getChoices().indexOf(choice) + 1 + "_ " + choice));
      System.out.println("Votre réponse : ");
      Scanner scanner = new Scanner(System.in);
      int answer = getUserAnswer(numberOfChoices, scanner);
      System.out.println("Vous avez répondu : " + answer);
      System.out.println("La bonne réponse était : " + (question.getAnswerId() + 1));
      if (answer == question.getAnswerId() + 1) {
        score++;
      }
      System.out.println("Appuyez sur entrée pour continuer");
      System.in.read();
      System.out.println("--------------------------------------------------");
    }
    System.out.println("Your Score: " + score);
  }
  
  private static int getUserAnswer(int numberOfChoices, Scanner scanner) {
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
}
