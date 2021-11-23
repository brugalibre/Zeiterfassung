package com.adcubum.timerecording.ticketbacklog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationBuilder;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderBuilder;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import com.adcubum.timerecording.ticketbacklog.defaulttickets.DefaultTicketReader;
import com.adcubum.timerecording.workerfactory.ThreadFactory;

public class TicketBacklogImpl implements TicketBacklog {

   private static final Logger LOG = Logger.getLogger(TicketBacklogImpl.class);
   private JiraApiReader jiraApiReader;
   private TicketBacklogHelper backlogHelper;
   private Set<Ticket> tickets;
   private FileImporter fileImporter;

   TicketBacklogImpl() {
      this(JiraApiReaderBuilder.of()
            .withJiraApiConfiguration(JiraApiConfigurationBuilder.of()
                  .withDefaultJiraApiConfiguration()
                  .build())
            .build(), FileImporterFactory.createNew());
   }

   /**
    * Constructor for testing purpose only!
    * 
    * @param jiraApiReader
    *        the {@link JiraApiReader}
    */
   TicketBacklogImpl(JiraApiReader jiraApiReader, FileImporter fileImporter) {
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

   @Override
   public Ticket getTicket4Nr(String ticketNr) {
      Ticket ticket = findExistingReadNewOrBuildDummyTicket(ticketNr);
      if (ticket.isBookable()) {
         tickets.add(ticket);
      }
      return ticket;
   }

   /*
    * We always return a Ticket.
    *    - First we're looking for an existing one.
    *    - If we don't find anything, we'll ask jira
    *    - If Jira don't find anything, we create an empty Ticket with the Ticket-Nr as the only set attribute
    */
   private Ticket findExistingReadNewOrBuildDummyTicket(String ticketNr) {
      applyJiraApiConfiguration(backlogHelper.getJiraBaseUrl());
      return tickets.stream()
            .filter(existingTicket -> existingTicket.getNr().equals(ticketNr))
            .findFirst()
            .orElseGet(() -> jiraApiReader.readTicket4Nr(ticketNr)
                  .orElseGet(() -> TicketFactory.INSTANCE.dummy(ticketNr)));
   }

   @Override
   public void initTicketBacklog(UiTicketBacklogCallbackHandler callbackHandler) {
      applyJiraApiConfiguration(backlogHelper.getJiraBaseUrl());
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

   private void applyJiraApiConfiguration(String jiraBaseUrl) {
      jiraApiReader.applyJiraApiConfiguration(JiraApiConfigurationBuilder.of()
            .withDefaultJiraApiConfiguration()
            .withNullableJiraAgileBasePath(jiraBaseUrl)
            .build());
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

   @Override
   public List<Ticket> getTickets() {
      return List.copyOf(tickets);
   }
}
