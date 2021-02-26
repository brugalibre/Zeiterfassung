package com.myownb3.dominic.timerecording.core.charge.coolguys;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.charge.ChargeException;
import com.myownb3.dominic.timerecording.core.charge.result.BookerResult;
import com.myownb3.dominic.timerecording.core.charge.result.BookResultType;

/**
 * The {@link BookerHelperResult} only exists if the booking was successful. Otherwise we throw an {@link ChargeException}
 * 
 * @author Dominic
 *
 */
public class BookerHelperResult implements BookerResult {

   @Override
   public boolean hasBooked() {
      return true;
   }

   @Override
   public BookResultType getBookResultType() {
      return BookResultType.SUCCESS;
   }

   @Override
   public String getMessage() {
      return TextLabel.SUCCESSFULLY_BOOKED_TEXT;
   }
}
