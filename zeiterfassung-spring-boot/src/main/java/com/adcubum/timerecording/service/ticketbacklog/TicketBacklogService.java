package com.adcubum.timerecording.service.ticketbacklog;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.model.ticketbacklog.ServiceCodeDto;
import com.adcubum.timerecording.model.ticketbacklog.TicketDto;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
      Ticket ticket = TicketBacklogSPI.getTicketBacklog().getTicket4Nr(ticketNr);
      return ticket.getTicketActivities()
              .stream()
              .map(toServiceCodeDto())
              .collect(Collectors.toList());
   }

   private static Function<TicketActivity, ServiceCodeDto> toServiceCodeDto() {
      return ticketActivity -> new ServiceCodeDto(ticketActivity.getActivityCode(), ticketActivity.getActivityName());
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
