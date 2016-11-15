import java.io.PrintStream;
import static org.junit.Assert.*;
import org.junit.Test;
import org.quartz.impl.StdSchedulerFactory;

public class StdSchedulerFactorySynchronizedInitTest {

  /**
   * Test method for {@link org.quartz.impl.StdSchedulerFactory#getScheduler()}
   * @throws InterruptedException
   */

  @Test
  public final void initSchedulerConcurrently() throws InterruptedException {

      final PrintStream consoleOut = System.out;
      Thread t1 = new Thread(new Runnable() {
          public void run() {
            for (int i = 1; i<= 100000; i++) {
              System.setProperty(""+i, ""+i);
//              System.getProperties(); // is evil, because not synchronized
            }
          }
      });

      Thread t2 = new Thread(new Runnable() {
          public void run() {
              for (int i = 1; i<= 1; i++) {
                StdSchedulerFactory schedFact = new StdSchedulerFactory();
                try {
                    schedFact.getScheduler();
                } catch (Exception e) {
                  consoleOut.println("Scheduler thread:");
                  e.printStackTrace(consoleOut);
                  fail("Unexpected exception");
                }
              }
          }
      }); 

      t1.start();
      t2.start();

      t2.join();
      t1.join();
  }
}
