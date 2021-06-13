package com.adcubum.timerecording.ticketbacklog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.defaulttickets.DefaultTicketReader;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;
import com.adcubum.timerecording.workerfactory.ThreadFactory;

public class TicketBacklog {

   private static final Logger LOG = Logger.getLogger(TicketBacklog.class);
   private JiraApiReader jiraApiReader;
   private TicketBacklogHelper backlogHelper;
   private Set<Ticket> tickets;
   private FileImporter fileImporter;

   TicketBacklog() {
      this(JiraApiReader.INSTANCE, FileImporterFactory.createNew());
   }

   /**
    * Constructor for testing purpose only!
    * 
    * @param jiraApiReader
    *        the {@link JiraApiReader}
    */
   TicketBacklog(JiraApiReader jiraApiReader, FileImporter fileImporter) {
      this.backlogHelper = new TicketBacklogHelper();
      this.jiraApiReader = jiraApiReader;
      this.tickets = new HashSet<>();
      this.fileImporter = fileImporter;
   }

   private void readDefaultTickets() {
      List<Ticket> defaultTickets = new DefaultTicketReader(jiraApiReader, fileImporter).readDefaultTickets();
      LOG.info("Read " + defaultTickets.size() + " default tickets from outside the sprint");
      this.tickets.addAll(defaultTickets);
   }

   /**
    * Evaluates a a {@link Ticket} for the given ticket-nr. If there is no ticket with the given nr, the {@link JiraApiReader} will be
    * called in order to retrieve it. This {@link Ticket} will then be added to this Backlog if it exists and it's bookable
    * 
    * If there is no Ticket for the given number this method returns a dummy-Ticket with only it's ticket-nr set
    * 
    * @param ticketNr
    *        the given Ticket nr
    * 
    * @return a {@link Ticket}
    */
   public Ticket getTicket4Nr(String ticketNr) {
      Ticket ticket = findExistingReadNewOrBuildDummyTicket(ticketNr);
      if (ticket.isBookable()) {
         tickets.add(ticket);
      }
      return ticket;
   }

   /*
    * We return always a Ticket. 
    *    - First we're looking for an existing one.
    *    - If we don't find anything, we'll ask jira
    *    - If Jira don't find anything, we create an empty Ticket with the Ticket-Nr as the only set attribute
    */
   private Ticket findExistingReadNewOrBuildDummyTicket(String ticketNr) {
      return tickets.stream()
            .filter(existingTicket -> existingTicket.getNr().equals(ticketNr))
            .findFirst()
            .orElseGet(() -> jiraApiReader.readTicket4Nr(ticketNr)
                  .orElseGet(() -> Ticket.dummy(ticketNr)));
   }

   /**
    * Initialises this {@link TicketBacklog} and calls the given callback handler afterwards
    * 
    * @param callbackHandler
    *        the {@link UiTicketBacklogCallbackHandler}
    */
   public void initTicketBacklog(UiTicketBacklogCallbackHandler callbackHandler) {
      if (!backlogHelper.hasBordNameConfigured()) {
         readDefaultTickets();
         LOG.warn("Unable to read the tickets, no board name provided. Check your turbo-bucher.properties!");
         callbackHandler.onTicketBacklogUpdated(UpdateStatus.NOT_CONFIGURED);
         return;
      }
      initTicketBacklogAsync(callbackHandler);
   }

   private void initTicketBacklogAsync(UiTicketBacklogCallbackHandler callbackHandler) {
      String boardName = backlogHelper.getBoardName();
      List<String> sprintNames = backlogHelper.getSprintNames();
      ThreadFactory.INSTANCE.execute(() -> {
         JiraApiReadTicketsResult jiraApiReadTicketsResult = initTicketBacklog(boardName, sprintNames);
         callbackHandler.onTicketBacklogUpdated(evalStatus(jiraApiReadTicketsResult));
      });
   }

   private static UpdateStatus evalStatus(JiraApiReadTicketsResult jiraApiReadTicketsResult) {
      return jiraApiReadTicketsResult.isSuccess() ? UpdateStatus.SUCCESS : UpdateStatus.FAIL;
   }

   private JiraApiReadTicketsResult initTicketBacklog(String boardName, List<String> sprintNames) {
      JiraApiReadTicketsResult jiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoardAndSprints(boardName, sprintNames);
      if (jiraApiReadTicketsResult.isSuccess()) {
         this.tickets.clear();
         tickets.addAll(jiraApiReadTicketsResult.getTickets());
      }
      readDefaultTickets();
      return jiraApiReadTicketsResult;
   }

   /**
    * @return the {@link Ticket}s of this {@link TicketBacklog}
    */
   public List<Ticket> getTickets() {
      return Collections.unmodifiableList(new ArrayList<>(tickets));
   }
}
