package com.adcubum.timerecording.jira.jiraapi.mapresponse;

import com.adcubum.timerecording.jira.data.ticket.Ticket;

import java.util.Collections;
import java.util.List;

/**
 * The final result from reading from the jira api.
 * Contains only a {@link Ticket}s as a result if there were non errors
 */
public class JiraApiReadTicketsResult {

   private final List<Ticket> tickets;
   private final ResponseStatus responseStatus;

   private JiraApiReadTicketsResult(List<Ticket> tickets, ResponseStatus responseStatus) {
      this.tickets = tickets;
      this.responseStatus = responseStatus;
   }

   public static JiraApiReadTicketsResult of(List<Ticket> tickets, boolean isSuccessful) {
      return new JiraApiReadTicketsResult(tickets, evalResponseStatus(tickets, isSuccessful));
   }

   private static ResponseStatus evalResponseStatus(List<Ticket> tickets, boolean isSuccessful) {
      return isSuccessful ? ResponseStatus.SUCCESS
              : !tickets.isEmpty() ? ResponseStatus.PARTIAL_SUCCESS
              : ResponseStatus.FAILURE;
   }

   public static JiraApiReadTicketsResult failed() {
      return new JiraApiReadTicketsResult(Collections.emptyList(), ResponseStatus.FAILURE);
   }

   public List<Ticket> getTickets() {
      return Collections.unmodifiableList(tickets);
   }

   public ResponseStatus getResponseStatus() {
      return responseStatus;
   }
}
