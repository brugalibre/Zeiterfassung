package com.adcubum.timerecording.api.ticketbacklog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcubum.timerecording.model.ticketbacklog.ServiceCodeDto;
import com.adcubum.timerecording.model.ticketbacklog.TicketDto;
import com.adcubum.timerecording.service.ticketbacklog.TicketBacklogService;

@RequestMapping("/api/v1/ticketbacklog")
@RestController
public class TicketBacklogController {

   private TicketBacklogService ticketBacklogService;

   @Autowired
   public TicketBacklogController(TicketBacklogService ticketBacklogService) {
      this.ticketBacklogService = ticketBacklogService;
   }

   @GetMapping(path = "/fetchTicketsFromBacklog")
   public List<TicketDto> fetchTicketsFromBacklog() {
      return ticketBacklogService.fetchTicketsFromBacklog();
   }

   @GetMapping(path = "/fetchServiceCodes/{ticketNr}")
   public List<ServiceCodeDto> fetchServiceCodes(@PathVariable("ticketNr") String ticketNr) {
      return ticketBacklogService.fetchServiceCodes(ticketNr);
   }
}
