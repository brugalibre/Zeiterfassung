package com.adcubum.scheduler.helper;

import java.util.Calendar;

import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeUtil;

public class SchedulerTimeHelper {

   private SchedulerTimeHelper() {
      // private
   }

   /**
    *
    * Returns the next day of the week, considering the given {@link DateTime} instance.
    * Saturday and Sunnday are skipped. So if we have Thursday 08:00 it will return
    * Friday, 08:00. If we have Friday 08:00 when it will return Monday 08:00
    * 
    * @param time
    *        the {@link DateTime} from which we want the next day
    * @return the next day of the week
    */
   public static DateTime getNextDayOfWeek(DateTime time) {
      int oneDayInSeconds = 24 * 60 * 60;
      DateTime nextDate = time.addSeconds(oneDayInSeconds);

      int dayOfWeek = DateTimeUtil.getCalenderOfTheWeek(nextDate);
      if (dayOfWeek == Calendar.SATURDAY) {
         nextDate = nextDate.addSeconds(oneDayInSeconds);
         dayOfWeek = DateTimeUtil.getCalenderOfTheWeek(nextDate);
      }
      if (dayOfWeek == Calendar.SUNDAY) {
         nextDate = nextDate.addSeconds(oneDayInSeconds);
      }
      return nextDate;
   }

}
