package com.adcubum.timerecording.service.ticketbacklog;

import java.util.List;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.model.ticketbacklog.ServiceCodeDto;
import com.adcubum.timerecording.model.ticketbacklog.TicketDto;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;

@Service
public class TicketBacklogService {

   /**
    * Returns all possible services codes for the given Ticket-nr
    * 
    * @param ticketNr
    *        the given Ticket-Nr
    * @return all possible services codes for the given Ticket-nr
    */
   public List<ServiceCodeDto> fetchServiceCodes(String ticketNr) {
      ToIntFunction<String> serviceCodeDescMapper = getServiceCodeDescMapper();
      LongFunction<List<String>> serviceCodesProvider = getServiceCodesProvider();
      Ticket ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticketNr);
      return serviceCodesProvider.apply(ticket.getTicketAttrs().getProjectNr())
            .stream()
            .map(map2ServiceDescriptionDto(serviceCodeDescMapper))
            .collect(Collectors.toList());
   }

   private static Function<String, ServiceCodeDto> map2ServiceDescriptionDto(ToIntFunction<String> serviceCodeDescMapper) {
      return serviceCode -> new ServiceCodeDto(serviceCodeDescMapper.applyAsInt(serviceCode), serviceCode);
   }

   private static LongFunction<List<String>> getServiceCodesProvider() {
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      return serviceCodeAdapter::fetchServiceCodesForProjectNr;
   }

   private static ToIntFunction<String> getServiceCodeDescMapper() {
      ServiceCodeAdapter serviceCodeAdapter = BookerAdapterFactory.getServiceCodeAdapter();
      return serviceCodeAdapter::getServiceCode4Description;
   }

   /**
    * Fetches all {@link TicketDto}s available in the {@link TicketBacklog}
    * 
    * @return all {@link TicketDto}
    */
   public List<TicketDto> fetchTicketsFromBacklog() {
      return TicketBacklogSPI.getTicketBacklog().getTickets()
            .stream()
            .map(TicketDto::of)
            .collect(Collectors.toList());
   }
}
