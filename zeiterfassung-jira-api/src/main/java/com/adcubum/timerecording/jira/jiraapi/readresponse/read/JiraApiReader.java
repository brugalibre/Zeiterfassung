package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import java.util.List;
import java.util.Optional;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.security.login.auth.init.UserAuthenticatedObservable;

/**
 * Tries to evaluate the id of the scrum board as well as the id of the current sprint. If successful by doing so,
 * the final url to receive all issues for the current sprint is created and the reveived issues mapped into a
 * {@link JiraApiReadTicketsResult}.
 * 
 * Note that jira does not provide more than 50 results at once.
 * So thats why you probably need to fetch a second or third time in order to get all the results
 * 
 * @author Dominic
 *
 */
public interface JiraApiReader extends UserAuthenticatedObservable {

   /** The singleton instance of a {@link JiraApiReader} */
   public static final JiraApiReader INSTANCE = JiraApiReaderFactory.createNew();

   /**
    * Reads a single {@link Ticket} for the given Ticket-Nr
    * 
    * @param ticketNr
    *        the given Ticket nr
    * 
    * @return a {@link Optional} of a {@link Ticket}
    */
   Optional<Ticket> readTicket4Nr(String ticketNr);

   /**
    * Tries to read all issues for the given boards current and active sprint
    * using a get request and returns a {@link JiraApiReadTicketsResult}
    * 
    * @param boardName
    *        the board
    * @param sprintNames
    *        a list with sprint names
    * @return a {@link JiraApiReadTicketsResult} which contains any
    *         {@link Ticket}s if the request was successfully or none if not
    *         (see also {@link JiraApiReadTicketsResult#isSuccess()}
    */
   JiraApiReadTicketsResult readTicketsFromBoardAndSprints(String boardName, List<String> sprintNames);
}
