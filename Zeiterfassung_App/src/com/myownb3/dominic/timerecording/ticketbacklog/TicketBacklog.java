package com.myownb3.dominic.timerecording.ticketbacklog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.myownb3.dominic.timerecording.core.workerfactory.ThreadFactory;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.defaulttickets.DefaultTicketReader;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

public class TicketBacklog {

   private static final Logger LOG = Logger.getLogger(TicketBacklog.class);
   private JiraApiReader jiraApiReader;
   private TicketBacklogHelper backlogHelper;
   private Set<Ticket> tickets;

   TicketBacklog() {
      this(JiraApiReader.INSTANCE);
   }

   /**
    * Constructor for testing purpose only!
    * 
    * @param jiraApiReader
    *        the {@link JiraApiReader}
    */
   TicketBacklog(JiraApiReader jiraApiReader) {
      this.backlogHelper = new TicketBacklogHelper();
      this.jiraApiReader = jiraApiReader;
      init();
   }

   private void init() {
      jiraApiReader.init();
      this.tickets = new HashSet<>();
   }

   private void readDefaultTickets() {
      List<Ticket> defaultTickets = new DefaultTicketReader(jiraApiReader).readDefaultTickets();
      LOG.info("Read " + defaultTickets.size() + " default tickets from outside the sprint");
      this.tickets.addAll(defaultTickets);
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
      ThreadFactory.INSTANCE.execute(() -> {
         JiraApiReadTicketsResult jiraApiReadTicketsResult = initTicketBacklog(boardName);
         callbackHandler.onTicketBacklogUpdated(evalStatus(jiraApiReadTicketsResult));
      });
   }

   private static UpdateStatus evalStatus(JiraApiReadTicketsResult jiraApiReadTicketsResult) {
      return jiraApiReadTicketsResult.isSuccess() ? UpdateStatus.SUCCESS : UpdateStatus.FAIL;
   }

   private JiraApiReadTicketsResult initTicketBacklog(String boardName) {
      JiraApiReadTicketsResult jiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoard(boardName);
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
