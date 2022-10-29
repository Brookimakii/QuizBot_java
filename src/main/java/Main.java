import controller.CommandManager;
import exception.NoFileFound;
import java.io.IOException;
import java.util.Scanner;

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
    Scanner scan = new Scanner(System.in);
    String state = "";
    while (!state.equals("end")) {
      System.out.print("Enter command: ");
      String command = scan.next();
      try {
        state = CommandManager.command(command);
      } catch (InterruptedException | NoFileFound | IOException e) {
        throw new RuntimeException(e);
      }
    }
    
  }
}
