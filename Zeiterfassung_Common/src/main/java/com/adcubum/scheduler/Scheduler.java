
package com.adcubum.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adcubum.scheduler.helper.SchedulerTimeHelper;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.workerfactory.ThreadFactory;

/**
 * Once a {@link Scheduler} is started it calls all registered {@link Runnable}s as soon as the time is up
 * After that the {@link Scheduler} starts over again.
 * 
 * @author Dominic
 *
 */
public class Scheduler implements Runnable {

   private List<Runnable> callbackHandlers;
   private boolean isRunning;
   private boolean wasWaiting;

   private Time scheduleDate;
   private TimeUnit timeUnit;
   private long sleepInterval;

   /**
    * Creates a new {@link Scheduler}
    * 
    * @param time
    *        the desired {@link Time} to call the registered {@link Runnable}s
    * @param timeUnit
    *        the {@link TimeUnit} which defines the time of each sleep duration
    * @param sleepInterval
    *        the interval this Scheduler sleeps
    */
   public Scheduler(Time time, TimeUnit timeUnit, long sleepInterval) {
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
   
   /**
    * Starts this scheduler as a new Thread using the {@link ThreadFactory}
    */
   public void start() {
      ThreadFactory.INSTANCE.execute(this);
   }

   /*
    * Notify the listeners only if we actually 'waited'.
    * So if the Scheduler is started after the dead line, do not trigger the callbacks
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

   /**
    * Adds the given {@link Runnable} as a callback-handler for this Scheduler
    * 
    * @param callbackHandler
    *        the {@link Runnable}
    */
   public void addCallback(Runnable callbackHandler) {
      callbackHandlers.add(callbackHandler);
   }

   /**
    * Stops this {@link Scheduler}
    */
   public void stop() {
      this.isRunning = false;
   }

   /**
    * @return <code>true</code> if this Scheduler is stopped or <code>false</code> if not
    */
   public boolean isStoped() {
      return !isRunning;
   }
}
