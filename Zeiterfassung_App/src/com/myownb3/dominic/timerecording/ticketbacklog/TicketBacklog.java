package com.myownb3.dominic.timerecording.ticketbacklog;

import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_PW_VALUE_KEY;
import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_VALUE_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.myownb3.dominic.timerecording.core.workerfactory.ThreadFactory;
import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

public class TicketBacklog {

   private static final Logger LOG = Logger.getLogger(TicketBacklog.class);
   private JiraApiReader jiraApiReader;
   private TicketBacklogHelper backlogHelper;
   private List<Ticket> tickets;

   TicketBacklog() {
      this.backlogHelper = new TicketBacklogHelper();
      this.jiraApiReader = JiraApiReader.INSTANCE;
      this.tickets = getDefaultScrumAndMeetingTickets();
   }

   private List<Ticket> getDefaultScrumAndMeetingTickets() {
      List<Ticket> defaultScrumAndMeetingTickets = new ArrayList<>();
      defaultScrumAndMeetingTickets.add(Ticket.MEETING_TICKET);
      defaultScrumAndMeetingTickets.add(Ticket.SCRUM_TICKET);
      defaultScrumAndMeetingTickets.add(Ticket.SCRUM_ARBEITEN_TICKET);
      return defaultScrumAndMeetingTickets;
   }

   /**
    * Initialises this {@link TicketBacklog} and calls the given callback handler afterwards
    * 
    * @param callbackHandler
    *        the {@link UiTicketBacklogCallbackHandler}
    */
   public void initTicketBacklog(UiTicketBacklogCallbackHandler callbackHandler) {
      if (!backlogHelper.hasBordNameConfigured()) {
         LOG.warn("Unable to read the tickets, no board name provided. Check your turbo-bucher.properties!");
         callbackHandler.onTicketBacklogUpdated(UpdateStatus.NOT_CONFIGURED);
         return;
      }
      String username = Settings.INSTANCE.getSettingsValue(USER_NAME_VALUE_KEY);
      String pw = Settings.INSTANCE.getSettingsValue(USER_NAME_PW_VALUE_KEY);
      initTicketBacklogAsync(callbackHandler, username, pw);
   }

   private void initTicketBacklogAsync(UiTicketBacklogCallbackHandler callbackHandler, String username, String pw) {
      String boardName = backlogHelper.getBoardName();
      ThreadFactory.INSTANCE.execute(() -> {
         JiraApiReadTicketsResult jiraApiReadTicketsResult = initTicketBacklog(boardName, username, pw);
         callbackHandler.onTicketBacklogUpdated(evalStatus(jiraApiReadTicketsResult));
      });
   }

   private static UpdateStatus evalStatus(JiraApiReadTicketsResult jiraApiReadTicketsResult) {
      return jiraApiReadTicketsResult.isSuccess() ? UpdateStatus.SUCCESS : UpdateStatus.FAIL;
   }

   private JiraApiReadTicketsResult initTicketBacklog(String boardName, String username, String pw) {
      JiraApiReadTicketsResult jiraApiReadTicketsResult = jiraApiReader.readTicketsFromJira(boardName, username, pw);
      if (jiraApiReadTicketsResult.isSuccess()) {
         this.tickets.clear();
         this.tickets.addAll(getDefaultScrumAndMeetingTickets());
         tickets.addAll(jiraApiReadTicketsResult.getTickets());
      }
      return jiraApiReadTicketsResult;
   }

   /**
    * @return the {@link Ticket}s of this {@link TicketBacklog}
    */
   public List<Ticket> getTickets() {
      return Collections.unmodifiableList(tickets);
   }
}
