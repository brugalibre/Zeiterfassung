package com.adcubum.timerecording.work.date;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class TimeUtil {

   private TimeUtil() {
      // private
   }

   /**
    * Returns the {@link Time} instance of that {@link Time} object which has the maximal amount of miliseconds
    * or <code>null</code> if there is an empty list
    * Null values are going to be ignored
    * 
    * @param times
    *        the {@link Time} objects to test
    * @return the {@link Time} instance of that {@link Time} object which has the maximal amount of miliseconds
    */
   public static Time max(Time... times) {
      Time maxTime = null;
      for (Time time : times) {
         if (isNull(maxTime)
               || currentTimeInstanceIsGreater(maxTime, time)) {
            maxTime = time;
         }
      }
      return maxTime;
   }

   private static boolean currentTimeInstanceIsGreater(Time maxTime, Time time) {
      return nonNull(time)
            && time.getTime() > maxTime.getTime();
   }
}
