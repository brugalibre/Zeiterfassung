package com.adcubum.timerecording.messaging.receive.mapping.impl;

import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.receive.mapping.TicketMapper;
import com.adcubum.timerecording.settings.Settings;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The {@link TicketMapping} provides one static method in order to get a {@link TicketMapper} for
 * a sender and receiver of a {@link com.adcubum.timerecording.messaging.model.BookBusinessDayMessage}
 */
public class TicketMapping {

   public static TicketMapper getMapper(BookSenderReceiverId bookSenderId, BookSenderReceiverId bookReceiverId) {
      requireNonNull(bookReceiverId, "We need a sender id!");
      requireNonNull(bookReceiverId, "We need a receiver id!");
      switch (bookSenderId) {
         case NAG:
            return getMapperToMapFromNAG(bookReceiverId);
         case POST_FINANCE:
            return getMapperToMapFromPF(bookReceiverId);
         default:
            throw createMappingNotImplementedException(BookSenderReceiverId.POST_FINANCE, bookReceiverId);
      }
   }

   private static TicketMapper getMapperToMapFromPF(BookSenderReceiverId bookReceiverId) {
      switch (bookReceiverId) {
         case NAG:
            return new FromPfToNAGMapper(Settings.INSTANCE);
         case POST_FINANCE:
            return new NOOPMapper();
      }
      throw createMappingNotImplementedException(BookSenderReceiverId.POST_FINANCE, bookReceiverId);
   }

   private static TicketMapper getMapperToMapFromNAG(BookSenderReceiverId bookReceiverId) {
      if (bookReceiverId == BookSenderReceiverId.NAG) {
         return new NOOPMapper();
      }
      throw createMappingNotImplementedException(BookSenderReceiverId.NAG, bookReceiverId);
   }

   private static IllegalStateException createMappingNotImplementedException(BookSenderReceiverId bookSenderId, BookSenderReceiverId bookReceiverId) {
      return new IllegalStateException("Mapping from '" + bookSenderId + "' to '" + bookReceiverId + "' not implemented");
   }

}
