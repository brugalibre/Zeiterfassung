package com.adcubum.timerecording.workerfactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@link ThreadFactory} handles the creating of {@link Thread}s using the
 * {@link ExecutorService}
 * 
 * @author Dominic
 *
 */
public class ThreadFactory {

   public static final ThreadFactory INSTANCE = new ThreadFactory();
   private ExecutorService executorService;

   private ThreadFactory() {
      executorService = Executors.newCachedThreadPool();
   }

   /**
    * Executes the given {@link Runnable}
    * 
    * @param runnable
    *        the {@link Runnable} to execute
    */
   public void execute(Runnable runnable) {
      executorService.execute(runnable);
   }
}
