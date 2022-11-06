package services;

import java.io.IOException;
import java.util.Scanner;
import org.kohsuke.github.GitHub;
import repositories.PropertiesManager;

/**
 * This class is able to create a GitHub report.
 */
public class GitHubReport {
  static String gitToken = java.lang.System.getenv("GITHUB_TOKEN");
  static String gitIssueRepo = "Brookimakii/QuizBotIssue";
  
  /**
   * This method is used to send a report to the GitHub repository.
   *
   * @param title The title of the issue.
   * @param body The body of the issue.
   * @param category The category of the issue.
   */
  public static void sendReport(String title, String body, String category) {
    if (gitToken != null) {
      java.lang.System.out.println(gitToken);
      java.lang.System.out.println("Sending report to GitHub");
      try {
        GitHub client = GitHub.connectUsingOAuth(gitToken);
        client.getRepository(PropertiesManager.getProperty("issueRepo")).createIssue(title)
            .body(body).label(category).create();
        java.lang.System.out.println("Report sent to GitHub");
      } catch (IOException e) {
        java.lang.System.out.println("Failed to send report to GitHub");
        throw new RuntimeException(e);
      }
    } else {
      java.lang.System.out.println("No GitHub token found");
    }
  }
  
  /**
   * Send a question report.
   *
   * @param questionId the question id.
   */
  public static void prepareQuestionReport(long questionId) {
    Scanner scan = new Scanner(java.lang.System.in);
    
    java.lang.System.out.print("Enter title: ");
    String title = scan.nextLine();
    title = title.equals("") ? "Question " + questionId + " Report" : title;
    
    java.lang.System.out.print("Type of issue: ");
    String type = scan.nextLine();
    type = type.equals("") ? "Ø" : type;
    
    java.lang.System.out.print("Solution Proposed: ");
    String solution = scan.nextLine();
    solution = solution.equals("") ? "Ø" : solution;
    
    java.lang.System.out.print("Additional Information: ");
    String additional = scan.nextLine();
    additional = additional.equals("") ? "Ø" : additional;
    
    java.lang.System.out.print("User name: ");
    String user = scan.nextLine();
    user = user.equals("") ? "Ø" : user;
    
    String body = "Question ID: #" + questionId + "\n" + "Type of issue: " + type + "\n"
                  + "Solution Proposed: " + solution + "\n" + "More details: " + additional + "\n"
                  + "\n" + "Report by " + user + "\n";
    
    sendReport(title, body, "question");
  }
  
}
