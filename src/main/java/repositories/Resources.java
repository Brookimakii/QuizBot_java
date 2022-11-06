package repositories;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
  
  
  /**
   * This methode load the score list.
   *
   * @throws IOException if error occurs when reading the resource content.
   */
  public static QuizSave loadQuizNo(int id) throws IOException {
    URL url = classLoader.getResource(PropertiesManager.getProperty("quizFile"));
    
    if (url == null) {
      return null;
    }
    ArrayList<QuizSave> quizSaves = mapper.readValue(url, new TypeReference<>() {});
    
    System.out.println("Quiz" + quizSaves);
    return quizSaves.stream().filter(quiz -> quiz.getId() == id).findFirst().orElse(null);
  }
  
  public static int appendQuizId() throws IOException {
    InputStream inputStream =
        classLoader.getResourceAsStream(PropertiesManager.getProperty("quizFile"));
    if (inputStream == null) {
      return 1;
    }
    ArrayList<QuizSave> quizSaves =
        mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
            new TypeReference<>() {}
        );
    QuizSave quizSave = quizSaves.stream().max(Comparator.comparing(QuizSave::getId)).orElse(null);
    if (quizSave != null) {
      return quizSave.getId() + 1;
    }
    return -1;
  }
  
  /**
   * Save the quiz to the file.
   *
   * @param quizSave the quiz to save
   * @throws IOException if error occurs when writing the resource content.
   */
  public static void saveQuiz(QuizSave quizSave) throws IOException {
    mapper.writerWithDefaultPrettyPrinter();
    URL url = classLoader.getResource(PropertiesManager.getProperty("quizFile"));
    if (url == null) {
      System.out.println("scoreFile not found");
      return;
    }
    
    appendObjectToFile(quizSave, url);
  }
  
  //------------------------------------------------------------------------------------------------
  
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
    System.out.println(json);
    Files.write(file.toPath(), Collections.singleton(json));
  }
  
  private static <T> void appendObjectToFile(T element, URL url) throws IOException {
    File file = new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8));
    System.out.println("file: " + file);
    
    ArrayList<T> list;
    try {
      list = mapper.readValue(file, new TypeReference<>() {});
    } catch (MismatchedInputException e) {
      list = new ArrayList<>();
    }
    if (list == null) {
      list = new ArrayList<>();
    }
    list.forEach(System.out::println);
    System.out.println("---------------------");
    list.add(element);
    System.out.println("---------------------");
    list.forEach(System.out::println);
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
    
    Files.write(file.toPath(), Collections.singleton(json));
  }
}
