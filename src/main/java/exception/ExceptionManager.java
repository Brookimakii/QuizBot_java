package exception;

public class ExceptionManager {

  public static void handleException(Exception e) {
    System.out.println("Error: " + e.getMessage());
  }
}
