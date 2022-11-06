package model;

import exception.ExceptionManager;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import controller.BotEventListener;

public class Bot {
  @Getter private static JDABuilder builder;
  @Getter private static JDA jda;
  
  
  public Bot() {
    builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"));
    jda = builder.addEventListeners(new BotEventListener())
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .build();
  
  }
  
}
