package controller;

import exception.NoFileFound;
import java.io.IOException;
import services.PersistanceDriver;
import services.Quiz;

public class CommandManager {
  
  public static String command(String command)
      throws InterruptedException, NoFileFound, IOException {
    switch (command) {
      case "start" -> {
        System.out.println("Start");
        Quiz quiz = new Quiz(PersistanceDriver.loadQuestion(), 10, 3, 2, 10, 4);
        quiz.startQuiz();
      }
      case "reload" -> System.out.println("Reload");
      case "close" -> {
        return "end";
      }
      default -> System.out.println("Command not Recognized");
    }
    return "";
  }
}
