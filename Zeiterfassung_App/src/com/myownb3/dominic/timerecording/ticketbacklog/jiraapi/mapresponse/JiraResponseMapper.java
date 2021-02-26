package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueResponse;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssuesResponse;

/**
 * The {@link JiraResponseMapper} maps a {@link JiraIssuesResponse} (json-style) to an {@link JiraApiReadTicketsResult}
 * 
 * @author Dominic
 *
 */
public class JiraResponseMapper {

   public static final JiraResponseMapper INSTANCE = new JiraResponseMapper();

   private JiraResponseMapper() {
      // private 
   }

   /**
    * Mapps the given {@link JiraIssueResponse} into a {@link Ticket} {@link Optional}
    * If the fetch was not sucessfull, then {@link Optional#empty()} is returned instead
    * 
    * @param jiraIssueResponse
    *        the received {@link JiraIssueResponse}
    * @return a {@link Optional} of a {@link Ticket}
    */
   public Optional<Ticket> map2Ticket(JiraIssueResponse jiraIssueResponse) {
      if (!jiraIssueResponse.isSuccess()) {
         return Optional.empty();
      }
      JiraIssue jiraIssue = JiraIssue.of(jiraIssueResponse);
      return Optional.of(Ticket.of(jiraIssue, false, false));
   }

   /**
    * Mapps the given {@link JiraIssuesResponse} into a {@link JiraApiReadTicketsResult}
    * 
    * @param jiraIssuesResponse
    *        the received {@link JiraIssuesResponse}
    * @return a {@link JiraApiReadTicketsResult} with all the {@link Ticket}s
    */
   public JiraApiReadTicketsResult map2TicketResult(JiraIssuesResponse jiraIssuesResponse) {
      return jiraIssuesResponse.getIssues()
            .stream()
            .filter(JiraIssue::isNotSubtask)
            .map(Ticket::of)
            .distinct()
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                  map2JiraApiReadTicketsResult(jiraIssuesResponse)));
   }

   private static Function<List<Ticket>, JiraApiReadTicketsResult> map2JiraApiReadTicketsResult(JiraIssuesResponse jiraIssuesResponse) {
      return tickets -> JiraApiReadTicketsResult.of(tickets, jiraIssuesResponse.getException());
   }
}
