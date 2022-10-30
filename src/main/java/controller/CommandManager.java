package controller;

import exception.NoFileFound;
import java.io.IOException;
import java.util.Scanner;
import services.GitHubReport;
import services.PersistanceDriver;
import services.Quiz;

public class CommandManager {
  
  public static String command(String command)
      throws InterruptedException, NoFileFound, IOException {
    switch (command) {
      case "start" -> {
        System.out.println("Start");
        Quiz quiz = new Quiz(PersistanceDriver.loadQuestion(), 10, 0, 2, 10, 4);
        quiz.startQuiz();
      }
      case "reload" -> System.out.println("Reload");
      case "report" -> {
        System.out.println("Report");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter title: ");
        String title = scan.nextLine();
        System.out.print("Enter body: ");
        String body = scan.nextLine();
        System.out.print("Enter category: ");
        String category = scan.nextLine();
        GitHubReport.sendReport(title, body, category);
      }
      case "close" -> {
        return "end";
      }
      default -> System.out.println("Command not Recognized");
    }
    return "";
  }
}
