package com.adcubum.timerecording.app.book;

import com.adcubum.timerecording.app.TimeRecorder;

/**
 * The {@link TimeRecorderBookResult} is a result when calling {@link TimeRecorder#book()}
 */
public interface TimeRecorderBookResult {
   /**
    * Indicates weather or not there was a booking process
    * E.g. there is no booking if there are no (bookable) {@link com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement}s
    *
    * @return <cdoe>true</cdoe> if there was a booking process or <code>false</code> if not
    */
   boolean hasBooked();

   /**
    * Indicates weather or not all non-booked {@link com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement}s where booked
    *
    * @return <cdoe>true</cdoe> if all bookable {@link com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement}s where booked
    * or <code>false</code> if there is at least on {@link com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement} which remains unbooked
    */
   boolean hasAllBooked();
}
