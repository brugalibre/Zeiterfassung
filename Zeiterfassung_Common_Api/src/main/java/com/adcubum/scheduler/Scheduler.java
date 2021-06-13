package com.adcubum.scheduler;

import com.adcubum.timerecording.workerfactory.ThreadFactory;

/**
 * Once a {@link Scheduler} is started it calls all registered {@link Runnable}s as soon as the time is up
 * After that the {@link Scheduler} starts over again.
 * 
 * @author Dominic
 *
 */
public interface Scheduler extends Runnable {

   /**
    * @return <code>true</code> if this SchedulerImpl is stopped or <code>false</code> if not
    */
   boolean isStoped();

   /**
    * Stops this {@link SchedulerImpl}
    */
   void stop();

   /**
    * Adds the given {@link Runnable} as a callback-handler for this SchedulerImpl
    * 
    * @param callbackHandler
    *        the {@link Runnable}
    */
   void addCallback(Runnable callbackHandler);

   /**
    * Starts this scheduler as a new Thread using the {@link ThreadFactory}
    */
   void start();

}
