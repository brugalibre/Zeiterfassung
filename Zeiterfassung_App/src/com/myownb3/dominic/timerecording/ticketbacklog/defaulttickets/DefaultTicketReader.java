package com.myownb3.dominic.timerecording.ticketbacklog.defaulttickets;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.myownb3.dominic.timerecording.core.importexport.in.file.FileImporter;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

/**
 * The {@link DefaultTicketReader} reads the default {@link Ticket}s which are always available even if the scrum tickets could not be read
 * 
 * @author Dominic
 *
 */
public class DefaultTicketReader {

   private String path2UserDefinedDefaultTickets;
   private JiraApiReader jiraApiReader;

   public DefaultTicketReader(JiraApiReader jiraApiReader) {
      this(jiraApiReader, DefaultTicketConst.DEFAULT_TICKETS_FILE);
   }

   /**
    * Constructor for testing purpose only
    */
   DefaultTicketReader(JiraApiReader jiraApiReader, String path2UserDefinedDefaultTickets) {
      this.jiraApiReader = requireNonNull(jiraApiReader);
      this.path2UserDefinedDefaultTickets = requireNonNull(path2UserDefinedDefaultTickets);
   }

   /**
    * Reads the default {@link Ticket}s which are defined in the {@link DefaultTicketConst} and in a optional file which
    * contains additionally default {@link Ticket}s
    * 
    * @return a List with default {@link Ticket}s
    */
   public List<Ticket> readDefaultTickets() {
      List<Ticket> defaultTickets = new ArrayList<>();
      List<String> defaultTicketNrs = getAllDefaultTicketNrs();
      for (String ticketNr : defaultTicketNrs) {
         Optional<Ticket> readTicket4Nr = jiraApiReader.readTicket4Nr(ticketNr.trim().toUpperCase());
         if (readTicket4Nr.isPresent()) {
            defaultTickets.add(readTicket4Nr.get());
         }
      }
      return defaultTickets;
   }

   private List<String> getAllDefaultTicketNrs() {
      Set<String> defaultTicketNrs = new HashSet<>();
      List<String> userDefinedDefaultTicketNrs = evalUserDefinedDefaultTickets();
      defaultTicketNrs.addAll(DefaultTicketConst.getDefaultScrumtTicketNrs());
      defaultTicketNrs.addAll(userDefinedDefaultTicketNrs);
      return new ArrayList<>(defaultTicketNrs);
   }

   private List<String> evalUserDefinedDefaultTickets() {
      File userDefinedTicketsFile = new File(path2UserDefinedDefaultTickets);
      if (userDefinedTicketsFile.exists()) {
         return FileImporter.INTANCE.importFile(userDefinedTicketsFile);
      }
      return Collections.emptyList();
   }
}
