package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.core.book.adapter.BookerAdapterFactory;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.servicecode.ServiceCodeDto;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationProvider;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderBuilder;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import com.adcubum.timerecording.ticketbacklog.defaulttickets.DefaultTicketReader;
import com.adcubum.timerecording.workerfactory.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.adcubum.timerecording.jira.jiraapi.mapresponse.ResponseStatus.PARTIAL_SUCCESS;
import static com.adcubum.timerecording.jira.jiraapi.mapresponse.ResponseStatus.SUCCESS;

public class TicketBacklogImpl extends AbstractTicketBacklog {

   private static final Logger LOG = LoggerFactory.getLogger(TicketBacklogImpl.class);
   private final ServiceCodeAdapter serviceCodeAdapter;
   private JiraApiReader jiraApiReader;
   private Set<Ticket> tickets;
   private FileImporter fileImporter;

   public TicketBacklogImpl() {
      this(JiraApiReaderBuilder.of()
              .withJiraApiConfiguration(JiraApiConfigurationProvider.INSTANCE.getJiraApiConfiguration())
              .build(), FileImporterFactory.createNew(), BookerAdapterFactory.getServiceCodeAdapter());
   }

   /**
    * Constructor for testing purpose only!
    *
    * @param jiraApiReader
    *        the {@link JiraApiReader}
    * @param fileImporter
    *        the {@link FileImporter}
    * @param serviceCodeAdapter
    *        the {@link ServiceCodeAdapter}
    */
   TicketBacklogImpl(JiraApiReader jiraApiReader, FileImporter fileImporter, ServiceCodeAdapter serviceCodeAdapter) {
      super();
      this.jiraApiReader = jiraApiReader;
      this.tickets = new HashSet<>();
      this.fileImporter = fileImporter;
      this.serviceCodeAdapter = serviceCodeAdapter;
   }

   private void readDefaultTickets() {
      List<Ticket> defaultTickets = new DefaultTicketReader(jiraApiReader, fileImporter).readDefaultTickets();
      LOG.info("Read {} default tickets from outside the sprint", defaultTickets.size());
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
      applyJiraApiConfiguration();
      return tickets.stream()
              .filter(existingTicket -> existingTicket.getNr().equals(ticketNr))
              .findFirst()
              .orElseGet(() -> jiraApiReader.readTicket4Nr(ticketNr)
                      .orElseGet(() -> TicketFactory.INSTANCE.dummy(ticketNr)));
   }

   @Override
   public TicketActivity getTicketActivity4ServiceCode(int serviceCode) {
      ServiceCodeDto serviceCodeDto = serviceCodeAdapter.getServiceCode4Code(serviceCode);
      return TicketActivityFactory.INSTANCE.createNew(serviceCodeDto.getServiceCodeName(), serviceCodeDto.getServiceCode());
   }

   @Override
   public void initTicketBacklog() {
      applyJiraApiConfiguration();
      if (!backlogHelper.hasBordNameConfigured()) {
         readDefaultTickets();
         LOG.warn("Unable to read the tickets, no board name provided. Check your configuration!");
         notifyCallbackHandlers(UpdateStatus.NOT_CONFIGURED);
         return;
      }
      initTicketBacklogAsync();
   }

   private void initTicketBacklogAsync() {
      String boardName = backlogHelper.getBoardName();
      List<String> sprintNames = backlogHelper.getSprintNames();
      ThreadFactory.INSTANCE.execute(() -> {
         JiraApiReadTicketsResult jiraApiReadTicketsResult = initTicketBacklog(boardName, sprintNames);
         notifyCallbackHandlers(evalStatus(jiraApiReadTicketsResult));
      });
   }

   private void applyJiraApiConfiguration() {
      jiraApiReader.applyJiraApiConfiguration(JiraApiConfigurationProvider.INSTANCE.getJiraApiConfiguration());
   }

   private static UpdateStatus evalStatus(JiraApiReadTicketsResult jiraApiReadTicketsResult) {
      switch (jiraApiReadTicketsResult.getResponseStatus()){
         case SUCCESS:
            return UpdateStatus.SUCCESS;
         case PARTIAL_SUCCESS:
            return UpdateStatus.PARTIAL_SUCCESS;
         case FAILURE:// fall through
         default:
            return UpdateStatus.FAIL;
      }
   }

   private JiraApiReadTicketsResult initTicketBacklog(String boardName, List<String> sprintNames) {
      JiraApiReadTicketsResult jiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoardAndSprints(boardName, sprintNames);
      // Add tickets if fully successful or if we read a few tickets and this backlog is empty so far
      if (jiraApiReadTicketsResult.getResponseStatus() == SUCCESS
              || jiraApiReadTicketsResult.getResponseStatus() == PARTIAL_SUCCESS) {
         this.tickets.clear();
         tickets.addAll(jiraApiReadTicketsResult.getTickets());
      }
      readDefaultTickets();
      return jiraApiReadTicketsResult;
   }

   @Override
   protected boolean getIsTicketDescriptionRequired() {
      return false;// jira tickets don't need a description
   }

   @Override
   public List<Ticket> getTickets() {
      return List.copyOf(tickets);
   }
}
