package services;

import exception.EmbedPageOutOfBound;
import java.util.ArrayList;
import lombok.Getter;
import model.QuizSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import repositories.PropertiesManager;

/**
 * This class is used to build embed messages.
 */
public class EmbedMessageBuilder {
  private final EmbedBuilder embed = new EmbedBuilder();
  @Getter private final ArrayList<Button> buttons = new ArrayList<>();
  
  /**
   * This method is used to create a new embed message.
   *
   * @param pageNumber The page number of the embed message.
   */
  public void getHelpEmbed(int pageNumber) {
    String prefix = PropertiesManager.getProperty("prefix");
    if (prefix.length() >= 2) {
      prefix += " ";
    }
    
    switch (pageNumber) {
      case 1 -> {
        embed.setTitle("Help");
        embed.setDescription("This is a list of command that you can use with this bot.");
        embed.addField(prefix + "play", "Start the quiz", true);
        embed.addField(prefix + "help", "Show this page.", true);
        embed.setFooter("Page 1/4");
        embed.setColor(0x33cc33);
        buttons.add(Button.primary("help_page_1_first", Emoji.fromUnicode("⏪")));
        buttons.add(Button.primary("help_page_1", Emoji.fromUnicode("◀")));
        buttons.add(Button.danger("help_page_cancel", Emoji.fromUnicode("❌")));
        buttons.add(Button.primary("help_page_2", Emoji.fromUnicode("▶")));
        buttons.add(Button.primary("help_page_4_last", Emoji.fromUnicode("⏩")));
      }
      case 2 -> {
        embed.setTitle("Help");
        embed.setDescription(
            "This is a list of command that can be used when the quiz is prepared.");
        embed.addField(prefix + "start", "Start the quiz", true);
        embed.setFooter("Page 2/4");
        embed.setColor(0x33cc33);
        buttons.add(Button.primary("help_page_1_first", Emoji.fromUnicode("⏪")));
        buttons.add(Button.primary("help_page_1", Emoji.fromUnicode("◀")));
        buttons.add(Button.danger("help_page_cancel", Emoji.fromUnicode("❌")));
        buttons.add(Button.primary("help_page_3", Emoji.fromUnicode("▶")));
        buttons.add(Button.primary("help_page_4_last", Emoji.fromUnicode("⏩")));
      }
      case 3 -> {
        embed.setTitle("Pagination");
        embed.setDescription("Hello World! This is the third page");
        embed.setFooter("Page 3/4");
        embed.setColor(0x33cc33);
        buttons.add(Button.primary("help_page_1_first", Emoji.fromUnicode("⏪")));
        buttons.add(Button.primary("help_page_2", Emoji.fromUnicode("◀")));
        buttons.add(Button.danger("help_page_cancel", Emoji.fromUnicode("❌")));
        buttons.add(Button.primary("help_page_4", Emoji.fromUnicode("▶")));
        buttons.add(Button.primary("help_page_4_last", Emoji.fromUnicode("⏩")));
      }
      case 4 -> {
        embed.setTitle("Pagination");
        embed.setDescription("Hello World! This is the last page");
        embed.setFooter("Page 4/4");
        embed.setColor(0x33cc33);
        buttons.add(Button.primary("help_page_1_first", Emoji.fromUnicode("⏪")));
        buttons.add(Button.primary("help_page_3", Emoji.fromUnicode("◀")));
        buttons.add(Button.danger("help_page_cancel", Emoji.fromUnicode("❌")));
        buttons.add(Button.primary("help_page_4", Emoji.fromUnicode("▶")));
        buttons.add(Button.primary("help_page_4_last", Emoji.fromUnicode("⏩")));
      }
      default -> {
        throw new EmbedPageOutOfBound();
      }
    }
  }
  
  public MessageEmbed getMessageEmbed() {
    return embed.build();
  }
  
  /**
   * Send the embed message to the channel.
   *
   * @param settings The quiz settings.
   */
  public void setEmbedSetting(QuizSettings settings) {
    
    StringBuilder builder = new StringBuilder();
    settings.getPlayers().forEach(player -> builder.append("> ").append(player.getName()));
    
    embed.setTitle(settings.getTitle()).setDescription("This is the parameters of the quiz")
        .addField("Room Master", "", true).addField(settings.getRoomMaster().getName(), "", true)
        .addBlankField(false).addField("Number of questions", "", true)
        .addField(String.valueOf(settings.getQuestionNumber()), "", true).addBlankField(false)
        .addField("Number of choices", "", true)
        .addField(String.valueOf(settings.getChoicesNumber()), "", true).addBlankField(false)
        .addField("Time to choose an answer", "", true)
        .addField(String.valueOf(settings.getTimeToAnswer()), "", true).addBlankField(false)
        .addField("Time to before the answers are shown", "", true)
        .addField(String.valueOf(settings.getTimeToShowChoices()), "", true).addBlankField(false)
        .addField("Time to pass to the next question", "", true)
        .addField(String.valueOf(settings.getTimeToPassToNext()), "", true).addBlankField(false)
        .addField("Players", "", true).addField(builder.toString(), "", true);
  }
}
