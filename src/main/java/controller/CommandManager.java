package controller;

import exception.NoFileFound;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import model.QuizSettings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import repositories.PropertiesManager;
import repositories.Resources;
import services.DiscordQuiz;
import services.EmbedMessageBuilder;
import services.GitHubReport;
import services.Quiz;

/**
 * This class is used to manage the commands.
 */
public class CommandManager {
  /**
   * This method is used to manage the commands.
   *
   * @param command The command.
   * @return "end" if the program should end.
   * @throws InterruptedException If the thread is interrupted.
   * @throws NoFileFound          If the file is not found.
   * @throws IOException          If an I/O error occurs.
   */
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
  
  /**
   * This method is used to manage the commands.
   *
   * @param command The command to manage.
   * @param event   The event that triggered the command.
   * @throws IOException If an error occurs while loading the properties.
   */
  public static void command(String command, MessageReceivedEvent event) throws IOException {
    
    String status = getQuizStatus(event);
    switch (status) {
      case "in game" -> playing(command, event);
      case "setting" -> setting(command, event);
      case "out of game" -> outOfGame(command, event);
      default -> throw new IllegalStateException("Unexpected value: " + status);
    }
  }
  
  private static String getQuizStatus(MessageReceivedEvent event) {
    QuizSettings settings = getFilteredQuizSettings(event);
    
    return settings != null ? settings.isRunning() ? "in game" : "setting" : "out of game";
  }
  
  private static QuizSettings getFilteredQuizSettings(MessageReceivedEvent event) {
    return Resources.getSettings().stream()
        .filter(setting -> setting.getQuizThread().equals(event.getChannel().asThreadChannel()))
        .findFirst().orElse(null);
  }
  
  //------------------------------------------------------------------------------------------------
  private static void playing(String command, MessageReceivedEvent event) {
    QuizSettings settings = getFilteredQuizSettings(event);
    User user = event.getAuthor();
    if (user != settings.getRoomMaster()) {
      return;
    }
    event.getMessage().delete().complete();
    switch (command) {
      case "yes" -> settings.getQuiz().nextQuestion();
      case "no" -> settings.backup();
      default ->
          sendTemporaryMessage(settings.getQuizThread(), "Error: " + command + " not Recognized",
              5
          );
    }
    
  }
  
  //------------------------------------------------------------------------------------------------
  
  private static void setting(String command, MessageReceivedEvent event) {
    User user = event.getAuthor();
    MessageChannel channel = event.getChannel();
    Guild guild = event.getGuild();
    
    QuizSettings setting = getFilteredQuizSettings(event);
    int quizSettingId = Resources.getSettings().indexOf(setting);
    String key;
    String value;
    try {
      key = command.substring(0, command.indexOf(' ')).trim();
      value = command.substring(command.indexOf(' ')).trim();
    } catch (StringIndexOutOfBoundsException e) {
      key = command;
      value = "";
    }
    
    try {
      
      
      switch (key.toLowerCase()) {
        case "choices" -> setting.setChoicesNumber(Integer.parseInt(value));
        case "question" -> setting.setQuestionNumber(Integer.parseInt(value));
        case "tca" -> {
          if (value.equals("")) {
            sendTemporaryMessage(setting.getQuizThread(), "Please enter a value.", 5);
          }
          int maxTime = Integer.parseInt(PropertiesManager.getProperty("maxTimeToAnswer"));
          if (Integer.parseInt(value) > maxTime && Integer.parseInt(value) < 0) {
            sendTemporaryMessage(setting.getQuizThread(),
                "The value is too large or to small, please enter a value between 0 and 10.", 5
            );
          } else {
            setting.setTimeToAnswer(Integer.parseInt(value));
          }
        }
        case "tbs" -> {
          if (value.equals("")) {
            sendTemporaryMessage(setting.getQuizThread(), "Please enter a value.", 5);
          }
          int maxTime = Integer.parseInt(PropertiesManager.getProperty("maxTimeToShow"));
          if (Integer.parseInt(value) > maxTime && Integer.parseInt(value) < 0) {
            sendTemporaryMessage(setting.getQuizThread(),
                "The value is too large or to small, please enter a value between 0 and 10.", 5
            );
          } else {
            setting.setTimeToShowChoices(Integer.parseInt(value));
          }
        }
        case "ttn" -> {
          if (value.equals("")) {
            sendTemporaryMessage(setting.getQuizThread(), "Please enter a value.", 5);
          }
          int maxTime = Integer.parseInt(PropertiesManager.getProperty("maxTimeToNext"));
          if (Integer.parseInt(value) > maxTime && Integer.parseInt(value) <= 0) {
            sendTemporaryMessage(setting.getQuizThread(),
                "The value is too large or to small, please enter a value between 0 and 10.", 5
            );
          } else {
            setting.setTimeToPassToNext(Integer.parseInt(value));
          }
        }
        case "title" -> {
          setting.getOriginalMessage().editMessage(value).queue();
          setting.setTitle(value);
        }
        case "join" -> {
          if (value.equals("")) {
            if (!setting.getPlayers().contains(user)) {
              setting.addPlayer(user);
            }
          } else {
            if (!user.equals(setting.getRoomMaster())) {
              sendTemporaryMessage(
                  setting.getQuizThread(), "Only the room master can use this command.", 5);
            } else {
              setting.addPlayer(event.getMessage().getMentions().getMembers());
            }
          }
        }
        case "quit" -> {
          if (value.equals("")) {
            if (setting.getPlayers().contains(user)) {
              setting.removePlayer(user);
            }
          } else {
            if (!user.equals(setting.getRoomMaster())) {
              sendTemporaryMessage(
                  setting.getQuizThread(), "Only the room master can use this command.", 5);
            } else {
              setting.addPlayer(event.getMessage().getMentions().getMembers());
            }
          }
        }
        
        case "stop" -> {
          sendTemporaryMessage(
              setting.getQuizThread().getParentMessageChannel(), "Quiz Canceled", 5);
          setting.getQuizThread().delete().queue();
          setting.getOriginalMessage().delete().queue();
          Resources.deleteSetting(setting);
          setting = null;
        }
        case "start" -> {
          setting.start();
          QuizSettings finalSetting = setting;
          setting.getQuizThread().sendMessage("Confirm parameters ?")
              .queue(msg -> new DiscordQuiz(finalSetting, msg));
        }
        default ->
            sendTemporaryMessage(setting.getQuizThread(), "Error: " + command + " not Recognized",
                5
            );
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      sendTemporaryMessage(setting.getQuizThread(),
          "Error: for the command \"" + key + "\" a number is awaited. \"" + value
          + "\" isn't a number", 3
      );
    }
    if (setting != null) {
      event.getMessage().delete().queue();
      Resources.overwriteSetting(setting, quizSettingId);
      setting.getParameterMessage().editMessage(setting.toStyleString()).queue();
    }
  }
  
  //------------------------------------------------------------------------------------------------
  
  private static void outOfGame(String command, MessageReceivedEvent event) {
    MessageChannel channel = event.getChannel();
    switch (command) {
      case "help" -> {
        help(event.getChannel());
        event.getMessage().delete().queue();
      }
      case "start" -> {
        start(event);
        event.getMessage().delete().queue();
      }
      default -> sendTemporaryMessage(channel, "Error: " + command + " not Recognized", 5);
    }
  }
  
  private static void help(MessageChannel channel) {
    System.out.println("Help command");
    EmbedMessageBuilder builder = new EmbedMessageBuilder();
    builder.getHelpEmbed(1);
    
    channel.sendMessageEmbeds(builder.getMessageEmbed()).addActionRow(builder.getButtons()).queue();
  }
  
  private static void start(MessageReceivedEvent event) {
    System.out.println("Start command");
    QuizSettings settings = new QuizSettings(event.getAuthor());
    
    event.getChannel().sendMessage("New Quiz").queue(message -> {
      settings.setOriginalMessage(message);
      message.createThreadChannel("Quiz").queue(thread -> {
        settings.setQuizThread(thread);
        thread.sendMessage(settings.toStyleString()).queue(settings::setParameterMessage);
      });
    });
    
    Resources.addSetting(settings);
    
    
  }
  
  //------------------------------------------------------------------------------------------------
  
  private static void sendTemporaryMessage(MessageChannel channel, String message, int time) {
    channel.sendMessage(message).queue(msg -> msg.delete().queueAfter(time, TimeUnit.SECONDS));
  }
}
