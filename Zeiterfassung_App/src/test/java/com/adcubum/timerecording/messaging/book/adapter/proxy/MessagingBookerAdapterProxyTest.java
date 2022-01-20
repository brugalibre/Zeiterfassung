package com.adcubum.timerecording.messaging.book.adapter.proxy;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.messaging.send.BookBusinessDayMessageSender;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MessagingBookerAdapterProxyTest {

   @Test
   void testSuccessfullyBook() {

      // Given
      String message = "Hurray!";
      BusinessDayImpl businessDay = new BusinessDayImpl();
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mockBookBusinessDayMessageSender(businessDay);
      BookerAdapter bookerAdapter = mockBookerAdapter(new TestBookerResult(businessDay, message), businessDay);

      MessagingBookerAdapterProxy messagingBookerAdapterProxy = new MessagingBookerAdapterProxy(bookerAdapter, bookBusinessDayMessageSender);

      // When
      BookerResult actualBookerResult = messagingBookerAdapterProxy.book(businessDay);

      // Then
      assertThat(actualBookerResult.getMessage(), is(message));
      assertThat(actualBookerResult.getBookedBusinessDay(), is(businessDay));
   }
   @Test
   void testDelegateMethods() {

      // Given
      Ticket ticket = mock(Ticket.class);
      BusinessDayImpl businessDay = new BusinessDayImpl();
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mockBookBusinessDayMessageSender(businessDay);
      BookerAdapter bookerAdapter = mockBookerAdapter(new TestBookerResult(businessDay, "msg"), businessDay);

      MessagingBookerAdapterProxy messagingBookerAdapterProxy = new MessagingBookerAdapterProxy(bookerAdapter, bookBusinessDayMessageSender);

      // When
      messagingBookerAdapterProxy.init();
      messagingBookerAdapterProxy.getServiceCodeAdapter();
      messagingBookerAdapterProxy.isTicketBookable(ticket);

      // Then
      verify(bookerAdapter).init();
      verify(bookerAdapter).getServiceCodeAdapter();
      verify(bookerAdapter).isTicketBookable(eq(ticket));
   }

   private static BookBusinessDayMessageSender mockBookBusinessDayMessageSender(BusinessDayImpl businessDay) {
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
      when(bookBusinessDayMessageSender.sendBookedIncrements(eq(businessDay))).thenReturn(businessDay);
      return bookBusinessDayMessageSender;
   }

   private static BookerAdapter mockBookerAdapter(BookerResult bookerResult, BusinessDay businessDay) {
      BookerAdapter bookerAdapter = mock(BookerAdapter.class);
      when(bookerAdapter.book(eq(businessDay))).thenReturn(bookerResult);
      return bookerAdapter;
   }

   private static class TestBookerResult implements BookerResult {
      private final BusinessDayImpl businessDay;
      private String message;

      public TestBookerResult(BusinessDayImpl businessDay, String message) {
         this.businessDay = businessDay;
         this.message = message;
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
      public String getMessage() {
         return message;
      }

      @Override
      public BusinessDay getBookedBusinessDay() {
         return businessDay;
      }
   }
}