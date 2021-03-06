package com.adcubum.timerecording.jira.defaulttickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.adcubum.timerecording.jira.data.ticket.Ticket;

/**
 * Contains constants for the default {@link Ticket}s
 * 
 * @author Dominic
 *
 */
public class DefaultTicketConst {

   private DefaultTicketConst() {
      // private
   }

   /** Name of the Text-file which contains the default {@link Ticket}s */
   public static final String DEFAULT_TICKETS_FILE = "default-tickets.txt";
   private static final String SCRUM_ARBEITEN_TICKET_NR = "INTA-155";
   private static final String SCRUM_TICKET_NR = "INTA-151";
   private static final String MEETING_TICKET_NR = "INTA-147";

   /**
    * @return the default scrum ticket nrs
    */
   public static final List<String> getDefaultScrumtTicketNrs() {
      List<String> defaultScrumAndMeetingTicketNrs = new ArrayList<>();
      defaultScrumAndMeetingTicketNrs.add(MEETING_TICKET_NR);
      defaultScrumAndMeetingTicketNrs.add(SCRUM_TICKET_NR);
      defaultScrumAndMeetingTicketNrs.add(SCRUM_ARBEITEN_TICKET_NR);
      return Collections.unmodifiableList(defaultScrumAndMeetingTicketNrs);
   }
}
