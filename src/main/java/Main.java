import controller.CommandManager;
import exception.NoFileFound;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import repositories.PropertiesManager;

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
    String rootPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    String appConfigPath = rootPath + "app.properties";
  
    Properties appProps = new Properties();
    try {
      appProps.load(new FileInputStream(appConfigPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String repo = appProps.getProperty("description");
    System.out.println("Issue repo: " + repo);
    
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
