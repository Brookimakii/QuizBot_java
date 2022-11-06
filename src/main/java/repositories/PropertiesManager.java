package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * This Class is able to save and load resources.
 */
public class PropertiesManager {
  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private static final ObjectMapper mapper = new ObjectMapper();
  private static Properties appProps = new Properties();
  
  static {
    
    URL url = classLoader.getResource("");
    if (url == null) {
      System.out.println("app.properties not found");
      throw new RuntimeException("app.properties not found");
    }
    String rootPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    String appConfigPath = rootPath + "app.properties";
    try {
      appProps.load(new FileInputStream(appConfigPath));
      load();
    } catch (IOException e) {
      appProps = null;
      System.out.println("Error while loading app.properties");
      throw new RuntimeException(e);
    }
    
  }
  
  /**
   * Load resources.
   *
   * @throws IOException if an error occurs while loading resources
   */
  public static void load() throws IOException {
    try {
      Resources.loadQuestion();
    } catch (IOException e) {
      System.out.println("Error while loading questionFile");
      throw e;
    }
    try {
      Resources.loadScore();
    } catch (IOException e) {
      System.out.println("Error while loading scoreFile");
      throw e;
    }
  }
  
  public static String getProperty(String key) {
    return appProps.getProperty(key);
  }
  
}
