package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import lombok.Getter;
import model.Question;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;

/**
 * This class is used to load resources.
 */
public class Resources {
  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private static final ObjectMapper mapper = new ObjectMapper();
  @Getter private static ArrayList<Question> questions;
  @Getter private static ArrayList<String> scores;
  @Getter private static final ArrayList<QuizSettings> settings = new ArrayList<>();
  
  
  
  /**
   * This methode load the question list.
   *
   * @throws IOException if error occurs when reading the resource content.
   */
  public static void loadQuestion() throws IOException {
    InputStream inputStream =
        classLoader.getResourceAsStream(PropertiesManager.getProperty("questionFile"));
    if (inputStream == null) {
      questions = new ArrayList<>();
      return;
    }
    Resources.questions = mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
        new TypeReference<>() {}
    );
  }
  
  /**
   * This methode load the score list.
   *
   * @throws IOException if error occurs when reading the resource content.
   */
  public static void loadScore() throws IOException {
    InputStream inputStream =
        classLoader.getResourceAsStream(PropertiesManager.getProperty("scoreFile"));
    if (inputStream == null) {
      scores = new ArrayList<>();
      return;
    }
    scores = mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
        new TypeReference<>() {}
    );
  }
  
  /**
   * Save the question to the file.
   *
   * @param questions the questions to save
   * @throws IOException if error occurs when writing the resource content.
   */
  public static void saveQuestion(ArrayList<Question> questions) throws IOException {
    mapper.writerWithDefaultPrettyPrinter();
    URL url = classLoader.getResource(PropertiesManager.getProperty("questionFile"));
    if (url == null) {
      java.lang.System.out.println("questionFile not found");
      return;
    }
    writeObjectInFile(questions, url);
  }
  
  /**
   * Save the scores to the file.
   *
   * @param scores the scores to save
   * @throws IOException if error occurs when writing the resource content.
   */
  public static void saveScore(ArrayList<String> scores) throws IOException {
    mapper.writerWithDefaultPrettyPrinter();
    URL url = classLoader.getResource(PropertiesManager.getProperty("scoreFile"));
    if (url == null) {
      java.lang.System.out.println("scoreFile not found");
      return;
    }
    writeObjectInFile(scores, url);
  }
  
  public static void addSetting(QuizSettings setting) {
    settings.add(setting);
  }
  
  public static void overwriteSetting(QuizSettings setting, int id) {
    settings.set(id, setting);
  }
  
  public static void deleteSetting(QuizSettings setting) {
    settings.remove(setting);
  }
  
  /**
   * This method filter the settings for the current game.
   *
   * @param event the event
   * @return the settings
   */
  public static QuizSettings getFilteredSetting(MessageChannelUnion event) {
    return Resources.getSettings().stream()
        .filter(setting -> setting.getQuizThread().equals(event.asThreadChannel())).findFirst()
        .orElse(null);
    
  }
  
  private static <T> void writeObjectInFile(ArrayList<T> element, URL url) throws IOException {
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
    File file = new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8));
    java.lang.System.out.println(json);
    Files.write(file.toPath(), Collections.singleton(json));
  }
}
