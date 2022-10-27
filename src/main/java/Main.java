import exception.NoFileFound;
import java.io.IOException;
import services.PersistanceDriver;
import services.Quiz;

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
      Quiz quiz = new Quiz(PersistanceDriver.loadQuestion(), 10, 3, 2, 10, 4);
      quiz.startQuiz();
    } catch (IOException | NoFileFound | InterruptedException e) {
      throw new RuntimeException(e);
    }
  
  }
}
