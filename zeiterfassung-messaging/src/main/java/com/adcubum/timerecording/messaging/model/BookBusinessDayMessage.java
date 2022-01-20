package com.adcubum.timerecording.messaging.model;

import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;

import static java.util.Objects.requireNonNull;

/**
 * A request to book a {@link BusinessDayDto} to send as kafka-message
 */
public class BookBusinessDayMessage {

   private BusinessDayDto businessDayDto;
   private BookSenderReceiverId bookSenderId;

   public BookBusinessDayMessage() {
      // empty
   }

   @Override
   public String toString() {
      return "BookBusinessDayMessage{" +
              "businessDayDto=" + businessDayDto +
              ", bookSenderId=" + bookSenderId +
              '}';
   }

   public static BookBusinessDayMessage of(BusinessDayDto businessDayDto, BookSenderReceiverId bookSenderId) {
      return buildBookBusinessDayMessage(bookSenderId, businessDayDto);
   }

   private static BookBusinessDayMessage buildBookBusinessDayMessage(BookSenderReceiverId bookSenderId, BusinessDayDto businessDayDto) {
      BookBusinessDayMessage bookBusinessDayMessage = new BookBusinessDayMessage();
      bookBusinessDayMessage.setBusinessDayDto(businessDayDto);
      bookBusinessDayMessage.setBookSenderId(bookSenderId);
      return bookBusinessDayMessage;
   }

   public BusinessDayDto getBusinessDayDto() {
      return businessDayDto;
   }

   public void setBusinessDayDto(BusinessDayDto businessDayDto) {
      this.businessDayDto = requireNonNull(businessDayDto);
   }

   public BookSenderReceiverId getBookSenderId() {
      return bookSenderId;
   }

   public void setBookSenderId(BookSenderReceiverId bookSenderId) {
      this.bookSenderId = requireNonNull(bookSenderId);
   }
}
