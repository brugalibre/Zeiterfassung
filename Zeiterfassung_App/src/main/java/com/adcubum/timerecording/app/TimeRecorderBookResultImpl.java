package com.adcubum.timerecording.app;

import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;

public class TimeRecorderBookResultImpl implements TimeRecorderBookResult {
   private final boolean hasBooked;
   private final boolean hasAllBooked;

   /**
    * Creates an empty, uninitialized {@link TimeRecorderBookResult}
    */
   private TimeRecorderBookResultImpl() {
      this(false, false);
   }

   private TimeRecorderBookResultImpl(boolean hasBooked, boolean hasAllBooked) {
      this.hasBooked = hasBooked;
      this.hasAllBooked = hasAllBooked;
   }

   /**
    * Creates a {@link TimeRecorderBookResult} of the given {@link BookerResult} returned by the {@link com.adcubum.timerecording.core.book.adapter.BookerAdapter}
    *
    * @param bookResult the {@link BookerResult}
    * @return a new TimeRecorderBookResult
    */
   public static TimeRecorderBookResult of(BookerResult bookResult) {
      return new TimeRecorderBookResultImpl(bookResult.hasBooked(), bookResult.getBookResultType() == BookResultType.SUCCESS);
   }
   /**
    * Creates a {@link TimeRecorderBookResult} for a successfully all-booked result
    *
    * @return a new TimeRecorderBookResult
    */
   public static TimeRecorderBookResult success() {
      return new TimeRecorderBookResultImpl(true, true);
   }

   /**
    * Creates a {@link TimeRecorderBookResult} for a non-booked result
    *
    * @return a new TimeRecorderBookResult
    */
   public static TimeRecorderBookResult nonBooked() {
      return new TimeRecorderBookResultImpl(false, false);
   }

   @Override
   public boolean hasBooked() {
      return hasBooked;
   }

   @Override
   public boolean hasAllBooked() {
      return hasAllBooked;
   }
}
