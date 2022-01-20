package com.adcubum.timerecording.messaging.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
public class BookBusinessDayMessageApiServiceHolder {
   private static BookBusinessDayMessageApiService bookBusinessDayMessageApiService;

   private BookBusinessDayMessageApiServiceHolder() {
      // private
   }

   public static BookBusinessDayMessageApiService getBookBusinessDayMessageApiService() {
      return bookBusinessDayMessageApiService;
   }

   @Autowired
   public void setBusinessDayEntityRepository(BookBusinessDayMessageApiService bookBusinessDayMessageApiService) {
      BookBusinessDayMessageApiServiceHolder.bookBusinessDayMessageApiService = bookBusinessDayMessageApiService;
   }
}
