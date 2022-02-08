package com.adcubum.timerecording.messaging.send;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.message.BookBusinessDayMessageApiAdapterService;
import com.adcubum.timerecording.messaging.api.BookBusinessDayMessageApiService;
import com.adcubum.timerecording.messaging.api.BookBusinessDayMessageApiServiceHolder;

public class BookBusinessDayMessageApiAdapterServiceImpl implements BookBusinessDayMessageApiAdapterService {

   private final BookBusinessDayMessageSender bookBusinessDayMessageSender;
   private final BookBusinessDayMessageApiService bookBusinessDayMessageApiService;

   BookBusinessDayMessageApiAdapterServiceImpl() {
      this.bookBusinessDayMessageSender = new BookBusinessDayMessageSender();
      this.bookBusinessDayMessageApiService = BookBusinessDayMessageApiServiceHolder.getBookBusinessDayMessageApiService();
   }

   @Override
   public BusinessDay sendBookedIncrements(BusinessDay businessDay) {
      return bookBusinessDayMessageSender.sendBookedIncrements(businessDay);
   }

   @Override
   public boolean canSendBookedBusinessDayIncrements() {
      return bookBusinessDayMessageSender.isSendBookedBusinessDayIncrementsEnabled();
   }

   @Override
   public void startListener() {
      bookBusinessDayMessageApiService.startListener();
   }
}
