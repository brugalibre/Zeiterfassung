package com.myownb3.dominic.timerecording.ticketbacklog;

import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_PW_VALUE_KEY;
import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_VALUE_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

public class TicketBacklog {

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
    * Initialises this {@link TicketBacklog}
    * 
    */
   public void initTicketBacklog() {
      if (!backlogHelper.hasBordNameConfigured()) {
         return;
      }
      String username = Settings.INSTANCE.getSettingsValue(USER_NAME_VALUE_KEY);
      String pw = Settings.INSTANCE.getSettingsValue(USER_NAME_PW_VALUE_KEY);
      initTicketBacklog(username, pw);
   }

   private void initTicketBacklog(String username, String pw) {
      String boardName = backlogHelper.getBoardName();
      JiraApiReadTicketsResult jiraApiReadTicketsResult = jiraApiReader.readTicketsFromJira(boardName, username, pw);
      if (jiraApiReadTicketsResult.isSuccess()) {
         this.tickets = getDefaultScrumAndMeetingTickets();
         tickets.addAll(jiraApiReadTicketsResult.getTickets());
      }
   }

   /**
    * @return the {@link Ticket}s of this {@link TicketBacklog}
    */
   public List<Ticket> getTickets() {
      return Collections.unmodifiableList(tickets);
   }
}
