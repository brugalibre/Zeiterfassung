package com.adcubum.timerecording.messaging.send;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.message.BookBusinessDayMessageApiService;

public class BookBusinessDayMessageApiServiceImpl implements BookBusinessDayMessageApiService {

   private BookBusinessDayMessageSender bookBusinessDayMessageSender;

   BookBusinessDayMessageApiServiceImpl() {
      this.bookBusinessDayMessageSender = new BookBusinessDayMessageSender();
   }

   @Override
   public BusinessDay sendBookedIncrements(BusinessDay businessDay) {
      return bookBusinessDayMessageSender.sendBookedIncrements(businessDay);
   }
}
