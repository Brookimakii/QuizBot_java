package services;

import java.io.IOException;
import java.util.Scanner;
import org.kohsuke.github.GitHub;

public class GitHubReport {
  static String gitToken = System.getenv("GITHUB_TOKEN");
  static String gitIssueRepo = "Brookimakii/QuizBotIssue";
  
  public static void sendReport(String title, String body, String category) {
    if (gitToken != null) {
      System.out.println(gitToken);
      System.out.println("Sending report to GitHub");
      try {
        GitHub client = GitHub.connectUsingOAuth(gitToken);
        client.getRepository(gitIssueRepo)
            .createIssue(title)
            .body(body)
            .label(category)
            .create();
        System.out.println("Report sent to GitHub");
      } catch (IOException e) {
        System.out.println("Failed to send report to GitHub");
        throw new RuntimeException(e);
      }
    }else {
      System.out.println("No GitHub token found");
    }
  }
  
  public static void prepareQuestionReport(long questionId){
    Scanner scan = new Scanner(System.in);
    
    System.out.print("Enter title: ");
    String title = scan.nextLine();
    title = title.equals("") ? "Question " + questionId + " Report" : title;
    
    System.out.print("Type of issue: ");
    String type = scan.nextLine();
    type = type.equals("") ? "Ø" : type;
    
    System.out.print("Solution Proposed: ");
    String solution = scan.nextLine();
    solution = solution.equals("") ? "Ø" : solution;
    
    System.out.print("Additional Information: ");
    String additional = scan.nextLine();
    additional = additional.equals("") ? "Ø" : additional;
    
    System.out.print("User name: ");
    String user = scan.nextLine();
    user = user.equals("") ? "Ø" : user;
    
    String body = "Question ID: #" + questionId + "\n"
        + "Type of issue: " + type + "\n"
        + "Solution Proposed: " + solution + "\n"
        + "More details: " + additional + "\n" + "\n"
        + "Report by " + user + "\n";
  
    sendReport(title, body, "question");
  }
}
