import controller.CommandManager;
import exception.NoFileFound;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import model.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import repositories.PropertiesManager;
import repositories.Resources;

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
    URL url = Main.class.getClassLoader().getResource("");
    if (url == null) {
      System.out.println("Error while loading app.properties");
      return;
    }
    try {
      PropertiesManager.load();
    } catch (IOException e) {
      java.lang.System.out.println("Error while loading resources");
      e.fillInStackTrace();
      throw new RuntimeException(e);
    }
    ArrayList<String> argList = new ArrayList<>(Arrays.stream(args).toList());
    if (argList.contains("--console")) {
      System.out.println("Console mode");
      consoleBot();
    }else{
      System.out.println("Discord mode");
      try {
        discordBot();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  private static void discordBot() throws IOException {
    Bot bot = new Bot();
  }
  
  private static void consoleBot() {
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
