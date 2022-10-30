package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.NoFileFound;
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
import org.apache.commons.io.IOUtils;

public class Resources {
  private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  private static final ObjectMapper mapper = new ObjectMapper();
  @Getter private static ArrayList<Question> questions;
  @Getter private static ArrayList<String> scores;
  
  
  
  /**
   * This methode load the question list.
   *
   * @throws IOException if error occurs when reading the resource content.
   */
  public static void loadQuestion() throws IOException {
    InputStream inputStream =
        classLoader.getResourceAsStream(PropertiesManagement.getProperty("questionFile"));
    if (inputStream == null) {
      questions = new ArrayList<>();
      return;
    }
    questions = mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
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
        classLoader.getResourceAsStream(PropertiesManagement.getProperty("scoreFile"));
    if (inputStream == null) {
      scores = new ArrayList<>();
      return;
    }
    scores = mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
        new TypeReference<>() {}
    );
  }
  
  
  public static void saveQuestion(ArrayList<Question> questions) throws IOException {
    mapper.writerWithDefaultPrettyPrinter();
    URL url = classLoader.getResource(PropertiesManagement.getProperty("questionFile"));
    if (url == null) {
      java.lang.System.out.println("questionFile not found");
      return;
    }
    writeObjectInFile(questions, url);
  }
  
  public static void saveScore(ArrayList<String> scores) throws IOException {
    mapper.writerWithDefaultPrettyPrinter();
    URL url = classLoader.getResource(PropertiesManagement.getProperty("scoreFile"));
    if (url == null) {
      java.lang.System.out.println("scoreFile not found");
      return;
    }
    writeObjectInFile(scores, url);
  }
  
  
  private static <T> void writeObjectInFile(ArrayList<T> element, URL url) throws IOException {
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
    File file = new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8));
    java.lang.System.out.println(json);
    Files.write(file.toPath(), Collections.singleton(json));
  }
}
