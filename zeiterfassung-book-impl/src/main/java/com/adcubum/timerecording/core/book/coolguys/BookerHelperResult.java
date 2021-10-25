package com.adcubum.timerecording.core.book.coolguys;

import static java.util.Objects.requireNonNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.coolguys.exception.ChargeException;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link BookerHelperResult} only exists if the booking was successful. Otherwise we throw an {@link ChargeException}
 * 
 * @author Dominic
 *
 */
public class BookerHelperResult implements BookerResult {

   private BusinessDay bookedBusinessDay;

   public BookerHelperResult(BusinessDay bookedBusinessDay) {
      this.bookedBusinessDay = requireNonNull(bookedBusinessDay);
   }

   @Override
   public boolean hasBooked() {
      return true;
   }

   @Override
   public BookResultType getBookResultType() {
      return BookResultType.SUCCESS;
   }

   @Override
   public BusinessDay getBookedBusinessDay() {
      return bookedBusinessDay;
   }

   @Override
   public String getMessage() {
      return TextLabel.SUCCESSFULLY_BOOKED_TEXT;
   }
}
