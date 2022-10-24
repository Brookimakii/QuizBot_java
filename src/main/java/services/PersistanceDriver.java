package services;

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
import model.Question;
import org.apache.commons.io.IOUtils;

public class PersistanceDriver {
  static private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  static private final ObjectMapper mapper = new ObjectMapper();
  static private final String questionFile = "question.json";
  static private final String scoreFile = "score.json";
  
  static {
    mapper.writerWithDefaultPrettyPrinter();
  }
  
  
  public static ArrayList<Question> loadQuestion() throws IOException, NoFileFound {
    InputStream inputStream = classLoader.getResourceAsStream(questionFile);
    fileExistenceCheck(inputStream);
    return mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
        new TypeReference<>() {}
    );
  }
  
  public static ArrayList<Question> loadScore() throws IOException, NoFileFound {
    InputStream inputStream = classLoader.getResourceAsStream(scoreFile);
    fileExistenceCheck(inputStream);
    return mapper.readValue(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
        new TypeReference<>() {}
    );
  }
  
  public static void saveQuestion(ArrayList<Question> questions) throws IOException, NoFileFound {
    URL url = classLoader.getResource(questionFile);
    fileExistenceCheck(url);
    writeObjectInFile(questions, url);
  }
  
  public static void saveScore(ArrayList<String> scores) throws IOException, NoFileFound {
    URL url = classLoader.getResource(scoreFile);
    fileExistenceCheck(url);
    writeObjectInFile(scores, url);
  }
  
  //------------------------------------------------------------------------------------------------
  
  private static <T> void writeObjectInFile(ArrayList<T> element, URL url) throws IOException {
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
    File file = new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8));
    
    System.out.println(json);
    Files.write(file.toPath(), Collections.singleton(json));
  }
  
  private static <T> void fileExistenceCheck(T element) throws NoFileFound {
    if (element == null) {
      throw new NoFileFound();
    }
  }
}
