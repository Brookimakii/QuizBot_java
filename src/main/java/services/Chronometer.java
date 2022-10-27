package services;

import exception.QuizEnded;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Getter;

/**
 * This class is a chronometer.
 */
public class Chronometer extends Timer {
  @Getter private TimerTask timerTask;
  @Getter private long actualTime;
  @Getter private final long timeoutTime;
  private final Object timeoutObject;
  private final String timeoutFunction;
  
  /**
   * This methode allow to create a chronometer.
   *
   * @param durationSecond  the duration of the chronometer in seconds.
   * @param timeoutObject   the object containing the timeout function.
   * @param timeoutFunction the timeout function name.
   */
  public Chronometer(long durationSecond, Object timeoutObject, String timeoutFunction) {
    
    this.timeoutTime = durationSecond * 1000;
    this.timeoutObject = timeoutObject;
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
          } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
                   | QuizEnded e) {
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
    timeoutObject.getClass().getMethod(timeoutFunction).invoke(timeoutObject);
    timerTask.cancel();
  }
  
  
  public long stop() {
    timerTask.cancel();
    return actualTime;
  }
}

