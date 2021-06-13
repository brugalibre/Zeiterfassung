package com.adcubum.scheduler;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import com.adcubum.util.parser.DateParser;

/**
 * The {@link SchedulerContainerImpl} can contains one or more {@link SchedulerImpl}s.
 * This {@link SchedulerContainerImpl} is used to manage those {@link SchedulerImpl}s
 * 
 * As soon as this reminds us this listener will then call a specific callback in order to react on the reminder
 * 
 * @author DStalder
 *
 */
public class SchedulerContainerImpl implements SchedulerContainer {

   private List<Scheduler> schedulers;
   private long schedulersSleepInterval;
   private TimeUnit timeUnit;

   /**
    * Creates a new {@link SchedulerContainerImpl and a new {@link SchedulerImpl}
    * If the reminder value of the given {@link UnaryOperator} is <code>null</code> then nothing happens!
    * The used {@link TimeUnit} for the {@link SchedulerImpl}s is {@link TimeUnit#MINUTES} and the interval value is 1
    * 
    * @param callbackHandler
    *        the callbackhandler which is called, as soon as the {@link SchedulerImpl} starts reminding
    */
   public SchedulerContainerImpl() {
      this(TimeUnit.MINUTES, 1);
   }

   /**
    * Creates a new {@link SchedulerContainerImpl} and a new {@link SchedulerImpl}
    * 
    * @param timeUnit
    *        the {@link TimeUnit} used by the created {@link SchedulerImpl}s
    * @param sleepInterval
    *        the interval the creaded SchedulerImpl sleeps
    */
   public SchedulerContainerImpl(TimeUnit timeUnit, long reminderSleepInterval) {
      this.schedulers = new ArrayList<>();
      this.schedulersSleepInterval = reminderSleepInterval;
      this.timeUnit = timeUnit;
   }

   @Override
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
      return new SchedulerImpl(DateParser.convertAndParse2Time(timeAsString, timeUnit), timeUnit, schedulersSleepInterval);
   }

   @Override
   public void start() {
      schedulers.stream()
            .forEach(Scheduler::start);
   }

   @Override
   public void stop() {
      schedulers.stream()
            .forEach(Scheduler::stop);
   }

   @Override
   public boolean isStoped() {
      return schedulers.stream()
            .allMatch(Scheduler::isStoped);
   }
}
