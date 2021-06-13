package com.adcubum.scheduler;

import java.util.concurrent.ThreadFactory;
import java.util.function.UnaryOperator;

/**
 * The {@link SchedulerContainer} can contains one or more {@link Scheduler}s.
 * This {@link SchedulerContainer} is used to manage those {@link Scheduler}s
 * 
 * As soon as this reminds us this listener will then call a specific callback in order to react on the reminder
 * 
 * @author DStalder
 *
 */
public interface SchedulerContainer {

   /**
    * @return <code>true</code> if all {@link SchedulerImpl} of this {@link SchedulerContainerImpl} are stopped or <code>false</code>
    *         if not
    */
   boolean isStoped();

   /**
    * Stops all {@link SchedulerImpl}
    */
   void stop();

   /**
    * Starts all added {@link SchedulerImpl} in a Thread using the {@link ThreadFactory}
    */
   void start();

   /**
    * Adds and a new {@link SchedulerImpl} which reminds at the start of a businessday
    * If the reminder value of the given {@link UnaryOperator} is <code>null</code> then nothing happens!
    * 
    * @param callbackHandler
    *        the callbackhandler which is called, as soon as the {@link SchedulerImpl} starts reminding
    * @param timeAsString
    *        the time value as String
    */
   void addScheduler(Runnable callbackHandler, String timeAsString);

}
