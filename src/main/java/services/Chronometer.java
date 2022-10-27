package services;

import exception.QuizEnded;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Getter;

public class Chronometer extends Timer {
  @Getter private TimerTask timerTask;
  @Getter private long actualTime;
  @Getter private final long timeoutTime;
  private final Object parent;
  private final String timeoutFunction;
  
  public Chronometer(long durationSecond, Object parent, String timeoutFunction) {
    
    this.timeoutTime = durationSecond * 1000;
    this.parent = parent;
    this.timeoutFunction = timeoutFunction;
    setupTimer();
  }
  
  private void setupTimer() {
    timerTask = new TimerTask() {
      @Override
      public void run() {
        /*if (actualTime % 1000 == 0) {
          //System.out.println(actualTime / 1000);
        }*/
        if (++actualTime > timeoutTime + 1) {
          try {
            timeout();
          } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                   QuizEnded e) {
            throw new RuntimeException(e);
          }
          this.cancel();
        }
      }
    };
    this.schedule(timerTask, 1000, 1);
  }
  
  
  private void timeout()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, QuizEnded {
    parent.getClass().getMethod(timeoutFunction).invoke(parent);
    timerTask.cancel();
  }
  
  
  public long stop() {
    timerTask.cancel();
    return actualTime;
  }
}

