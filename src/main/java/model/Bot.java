package model;

import controller.BotEventListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * This class represent the bot.
 */
public class Bot {
  @Getter private static JDABuilder builder;
  @Getter private static JDA jda;
  
  /**
   * This method is used to start the bot.
   */
  public Bot() {
    builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"));
    jda = builder.addEventListeners(new BotEventListener())
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .build();
  
  }
  
}
