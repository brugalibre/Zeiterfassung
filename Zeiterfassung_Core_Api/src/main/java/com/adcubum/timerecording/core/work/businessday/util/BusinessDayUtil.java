package com.adcubum.timerecording.core.work.businessday.util;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.util.parser.NumberFormat;

public class BusinessDayUtil {

   private BusinessDayUtil() {
      // private
   }

   /**
    * Returns total duration of the given {@link BusinessDay} as string
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return the total duration of the given {@link BusinessDay} as string
    */
   public static String getTotalDurationRep(BusinessDay businessDay) {
      return NumberFormat.format(businessDay.getTotalDuration());
   }

   /**
    * Returns total duration of the given {@link BusinessDayIncrement} as string
    * 
    * @param businessDay
    *        the given {@link BusinessDay}
    * @return the total duration of the given {@link BusinessDayIncrement} as string
    */
   public static String getTotalDurationRep(BusinessDayIncrement businessDayIncrement) {
      return NumberFormat.format(businessDayIncrement.getTotalDuration());
   }
}
