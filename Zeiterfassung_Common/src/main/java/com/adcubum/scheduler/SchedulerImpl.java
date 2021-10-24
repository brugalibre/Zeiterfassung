
package com.adcubum.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adcubum.scheduler.helper.SchedulerTimeHelper;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.workerfactory.ThreadFactory;

/**
 * Basic implementation of a {@link Scheduler}
 * After that the {@link SchedulerImpl} starts over again.
 * 
 * @author Dominic
 *
 */
public class SchedulerImpl implements Scheduler {

   private List<Runnable> callbackHandlers;
   private boolean isRunning;
   private boolean wasWaiting;

   private DateTime scheduleDate;
   private TimeUnit timeUnit;
   private long sleepInterval;

   /**
    * Creates a new {@link SchedulerImpl}
    * 
    * @param time
    *        the desired {@link DateTime} to call the registered {@link Runnable}s
    * @param timeUnit
    *        the {@link TimeUnit} which defines the time of each sleep duration
    * @param sleepInterval
    *        the interval this SchedulerImpl sleeps
    */
   public SchedulerImpl(DateTime time, TimeUnit timeUnit, long sleepInterval) {
      this.isRunning = true;
      this.wasWaiting = false;
      this.scheduleDate = requireNonNull(time);
      this.callbackHandlers = new ArrayList<>();
      this.timeUnit = requireNonNull(timeUnit);
      this.sleepInterval = sleepInterval;
   }

   @Override
   public void run() {
      while (isRunning) {
         waitUntil();
         notifyListeners();
         prepareForNextIteration();
      }
   }

   @Override
   public void start() {
      ThreadFactory.INSTANCE.execute(this);
   }

   /*
    * Notify the listeners only if we actually 'waited'.
    * So if the SchedulerImpl is started after the dead line, do not trigger the callbacks
    */
   private void notifyListeners() {
      if (isRunning && wasWaiting) {
         callbackHandlers.stream()
               .forEach(Runnable::run);
      }
   }

   private void waitUntil() {
      long timeToWait = calcTimeToWait();
      while (timeToWait > 0 && isRunning) {
         try {
            timeUnit.sleep(sleepInterval);
            timeToWait = calcTimeToWait();
            wasWaiting = true;
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            timeToWait = calcTimeToWait();
         }
      }
   }

   private void prepareForNextIteration() {
      this.scheduleDate = SchedulerTimeHelper.getNextDayOfWeek(scheduleDate);
      this.wasWaiting = false;
   }

   private long calcTimeToWait() {
      Date now = new Date();
      return scheduleDate.getTime() - now.getTime();
   }

   @Override
   public void addCallback(Runnable callbackHandler) {
      callbackHandlers.add(callbackHandler);
   }

   @Override
   public void stop() {
      this.isRunning = false;
   }

   @Override
   public boolean isStoped() {
      return !isRunning;
   }
}
