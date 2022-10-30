package services;

import java.io.IOException;
import org.kohsuke.github.GitHub;

public class GitHubReport {
  static String gitToken = System.getenv("GIT_TOKEN");
  static String gitIssueRepo = "Brookimakii/QuizBotIssue";
  
  public static void sendReport(String title, String body, String category) {
    if (gitToken != null) {
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
}
