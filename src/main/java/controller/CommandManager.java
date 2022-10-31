package controller;

import exception.NoFileFound;
import java.io.IOException;
import java.util.Scanner;
import repositories.Resources;
import services.GitHubReport;
import repositories.PropertiesManager;
import services.Quiz;

public class CommandManager {
  
  public static String command(String command)
      throws InterruptedException, NoFileFound, IOException {
    switch (command) {
      case "start" -> {
        java.lang.System.out.println("Start");
        Quiz quiz = new Quiz(Resources.getQuestions(), 10, 0, 2, 10, 4);
        quiz.startQuiz();
      }
      case "reload" -> PropertiesManager.load();
      case "report" -> {
        java.lang.System.out.println("Report");
        Scanner scan = new Scanner(java.lang.System.in);
        java.lang.System.out.print("Enter title: ");
        String title = scan.nextLine();
        java.lang.System.out.print("Enter body: ");
        String body = scan.nextLine();
        java.lang.System.out.print("Enter category: ");
        String category = scan.nextLine();
        GitHubReport.sendReport(title, body, category);
      }
      case "close" -> {
        return "end";
      }
      default -> java.lang.System.out.println("Command not Recognized");
    }
    return "";
  }
}
