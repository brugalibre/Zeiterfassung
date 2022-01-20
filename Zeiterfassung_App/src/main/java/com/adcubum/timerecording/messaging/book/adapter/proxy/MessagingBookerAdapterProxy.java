package com.adcubum.timerecording.messaging.book.adapter.proxy;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.messaging.send.BookBusinessDayMessageSender;

/**
 * The {@link MessagingBookerAdapterProxy} servers as a proxy for a {@link BookerAdapter} which does
 * sending booked {@link com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement} using the {@link BookBusinessDayMessageSender}
 *
 * @author dstalder
 */
public class MessagingBookerAdapterProxy implements BookerAdapter {
   private final BookerAdapter bookerAdapter;
   private final BookBusinessDayMessageSender bookBusinessDayMessageSender;

   public MessagingBookerAdapterProxy(BookerAdapter bookerAdapter, BookBusinessDayMessageSender bookBusinessDayMessageSender) {
      this.bookerAdapter = bookerAdapter;
      this.bookBusinessDayMessageSender = bookBusinessDayMessageSender;
   }

   @Override
   public void init() {
      bookerAdapter.init();
   }

   @Override
   public ServiceCodeAdapter getServiceCodeAdapter() {
      return bookerAdapter.getServiceCodeAdapter();
   }

   @Override
   public BookerResult book(BusinessDay businessDay) {
      BookerResult bookerResult = bookerAdapter.book(businessDay);
      BusinessDay sentBusinessDay = bookBusinessDayMessageSender.sendBookedIncrements(bookerResult.getBookedBusinessDay());
      return new BookerResultProxy(bookerResult, sentBusinessDay);
   }

   @Override
   public boolean isTicketBookable(Ticket ticket) {
      return bookerAdapter.isTicketBookable(ticket);
   }

   private static class BookerResultProxy implements BookerResult {
      private final BookerResult bookerResult;
      private final BusinessDay sentBusinessDay;

      private BookerResultProxy(BookerResult bookerResult, BusinessDay sentBusinessDay) {
         this.bookerResult = bookerResult;
         this.sentBusinessDay = sentBusinessDay;
      }

      @Override
      public boolean hasBooked() {
         return bookerResult.hasBooked();
      }

      @Override
      public BookResultType getBookResultType() {
         return bookerResult.getBookResultType();
      }

      @Override
      public String getMessage() {
         return bookerResult.getMessage();
      }

      @Override
      public BusinessDay getBookedBusinessDay() {
         return sentBusinessDay;
      }
   }
}
