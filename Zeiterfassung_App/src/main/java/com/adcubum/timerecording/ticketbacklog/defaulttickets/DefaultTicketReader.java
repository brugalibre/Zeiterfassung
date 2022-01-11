package com.adcubum.timerecording.ticketbacklog.defaulttickets;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.defaulttickets.DefaultTicketConst;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * The {@link DefaultTicketReader} reads the default {@link Ticket}s which are always available even if the scrum tickets could not be read
 * 
 * @author Dominic
 *
 */
public class DefaultTicketReader {

   private FileImporter fileImporter;
   private String path2UserDefinedDefaultTickets;
   private JiraApiReader jiraApiReader;

   /**
    * Creates a new {@link DefaultTicketReader} which reads the default {@link Ticket}s
    * 
    * @param jiraApiReader
    *        the {@link JiraApiReader}
    * @param fileImporter
    *        a {@link Function} which does the actual file import
    */
   public DefaultTicketReader(JiraApiReader jiraApiReader, FileImporter fileImporter) {
      this(jiraApiReader, DefaultTicketConst.DEFAULT_TICKETS_FILE, fileImporter);
   }

   /**
    * Constructor for testing purpose only
    * 
    * @param jiraApiReader
    *        the provided {@link JiraApiReader}
    * @param path2UserDefinedDefaultTickets
    *        the path to the default {@link Ticket}s
    * @param fileImporter
    *        the {@link Function} to import the files
    */
   DefaultTicketReader(JiraApiReader jiraApiReader, String path2UserDefinedDefaultTickets, FileImporter fileImporter) {
      this.jiraApiReader = requireNonNull(jiraApiReader);
      this.fileImporter = requireNonNull(fileImporter);
      this.path2UserDefinedDefaultTickets = requireNonNull(path2UserDefinedDefaultTickets);
   }

   /**
    * Reads the default {@link Ticket}s which are defined in an optional file which
    * contains default {@link Ticket}s
    * 
    * @return a List with default {@link Ticket}s
    */
   public List<Ticket> readDefaultTickets() {
      List<Ticket> defaultTickets = new ArrayList<>();
      List<String> defaultTicketNrs = getAllDefaultTicketNrs();
      for (String ticketNr : defaultTicketNrs) {
         jiraApiReader.readTicket4Nr(ticketNr.toUpperCase())
                 .ifPresent(defaultTickets::add);
      }
      return defaultTickets;
   }

   private List<String> getAllDefaultTicketNrs() {
      Set<String> defaultTicketNrs = new HashSet<>(evalUserDefinedDefaultTickets());
      return new ArrayList<>(defaultTicketNrs);
   }

   private List<String> evalUserDefinedDefaultTickets() {
      File userDefinedTicketsFile = new File(path2UserDefinedDefaultTickets);
      if (userDefinedTicketsFile.exists()) {
         return fileImporter.importFile(userDefinedTicketsFile);
      }
      return Collections.emptyList();
   }
}
