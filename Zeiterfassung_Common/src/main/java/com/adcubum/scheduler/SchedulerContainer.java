package com.adcubum.scheduler;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import com.adcubum.timerecording.workerfactory.ThreadFactory;
import com.adcubum.util.parser.DateParser;

/**
 * The {@link SchedulerContainer} can contains one or more {@link Scheduler}s.
 * This {@link SchedulerContainer} is used to manage those {@link Scheduler}s
 * 
 * As soon as this reminds us this listener will then call a specific callback in order to react on the reminder
 * 
 * @author DStalder
 *
 */
public class SchedulerContainer {

   private List<Scheduler> schedulers;
   private long schedulersSleepInterval;
   private TimeUnit timeUnit;

   /**
    * Creates a new {@link SchedulerContainer and a new {@link Scheduler}
    * If the reminder value of the given {@link UnaryOperator} is <code>null</code> then nothing happens!
    * The used {@link TimeUnit} for the {@link Scheduler}s is {@link TimeUnit#MINUTES} and the interval value is 1
    * 
    * @param callbackHandler
    *        the callbackhandler which is called, as soon as the {@link Scheduler} starts reminding
    */
   public SchedulerContainer() {
      this(TimeUnit.MINUTES, 1);
   }

   /**
    * Creates a new {@link SchedulerContainer} and a new {@link Scheduler}
    * 
    * @param timeUnit
    *        the {@link TimeUnit} used by the created {@link Scheduler}s
    * @param sleepInterval
    *        the interval the creaded Scheduler sleeps
    */
   public SchedulerContainer(TimeUnit timeUnit, long reminderSleepInterval) {
      this.schedulers = new ArrayList<>();
      this.schedulersSleepInterval = reminderSleepInterval;
      this.timeUnit = timeUnit;
   }

   /**
    * Adds and a new {@link Scheduler} which reminds at the start of a businessday
    * If the reminder value of the given {@link UnaryOperator} is <code>null</code> then nothing happens!
    * 
    * @param callbackHandler
    *        the callbackhandler which is called, as soon as the {@link Scheduler} starts reminding
    * @param timeAsString
    *        the time value as String
    */
   public void addScheduler(Runnable callbackHandler, String timeAsString) {
      createAndAddScheduler(callbackHandler, timeAsString);
   }

   private void createAndAddScheduler(Runnable callbackHandler, String timeAsString) {
      if (nonNull(timeAsString)) {
         Scheduler scheduler = createScheduler(timeAsString, timeUnit);
         scheduler.addCallback(callbackHandler);
         schedulers.add(scheduler);
      }
   }

   Scheduler createScheduler(String timeAsString, TimeUnit timeUnit) {
      return new Scheduler(DateParser.convertAndParse2Time(timeAsString, timeUnit), timeUnit, schedulersSleepInterval);
   }

   /**
    * Starts all added {@link Scheduler} in a Thread using the {@link ThreadFactory}
    */
   public void start() {
      schedulers.stream()
            .forEach(Scheduler::start);
   }

   /**
    * Stops all {@link Scheduler}
    */
   public void stop() {
      schedulers.stream()
            .forEach(Scheduler::stop);
   }

   /**
    * @return <code>true</code> if all {@link Scheduler} of this {@link SchedulerContainer} are stopped or <code>false</code>
    *         if not
    */
   public boolean isStoped() {
      return schedulers.stream()
            .allMatch(Scheduler::isStoped);
   }
}
