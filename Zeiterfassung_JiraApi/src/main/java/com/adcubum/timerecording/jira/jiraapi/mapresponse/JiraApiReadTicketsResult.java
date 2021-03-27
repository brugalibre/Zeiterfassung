package com.adcubum.timerecording.jira.jiraapi.mapresponse;

import java.util.Collections;
import java.util.List;

import com.adcubum.timerecording.jira.data.Ticket;

/**
 * The final result from reading from the jira api.
 * Contains only a {@link Ticket}s as a result if there were non errors
 */
public class JiraApiReadTicketsResult {

   private List<Ticket> tickets;
   private boolean isSuccess;

   private JiraApiReadTicketsResult(List<Ticket> tickets, boolean isSuccess) {
      this.tickets = tickets;
      this.isSuccess = isSuccess;
   }

   public static JiraApiReadTicketsResult of(List<Ticket> tickets, Exception exception) {
      return new JiraApiReadTicketsResult(tickets, exception == null);
   }

   public static JiraApiReadTicketsResult failed() {
      return new JiraApiReadTicketsResult(Collections.emptyList(), false);
   }

   public List<Ticket> getTickets() {
      return Collections.unmodifiableList(tickets);
   }

   public boolean isSuccess() {
      return isSuccess;
   }
}
