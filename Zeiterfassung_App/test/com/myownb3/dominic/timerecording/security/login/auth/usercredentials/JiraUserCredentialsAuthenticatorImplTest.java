package com.myownb3.dominic.timerecording.security.login.auth.usercredentials;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.myownb3.dominic.timerecording.test.BaseTestWithSettings;

class JiraUserCredentialsAuthenticatorImplTest extends BaseTestWithSettings {

   private static final String USERNAME = "hampi";

   @Test
   void testDoUserAuthentication_FetchWithDefaultTicket_TicketPresentButDummy() {
      // Given
      String ticketNr2TestConnection = "Ticket-4321";
      JiraApiReader jiraApiReader = mockJiraApiReader(ticketNr2TestConnection, Optional.of(Ticket.dummy(ticketNr2TestConnection)));
      JiraUserCredentialsAuthenticatorImpl jiraUserCredentialsAuthenticatorImpl =
            new JiraUserCredentialsAuthenticatorImpl(jiraApiReader, ticketNr2TestConnection);

      // When
      boolean actualIsUserAuthentication = jiraUserCredentialsAuthenticatorImpl.doUserAuthentication(USERNAME, "sdf".toCharArray());

      // Then
      assertThat(actualIsUserAuthentication, is(false));
   }

   @Test
   void testDoUserAuthentication_FetchWithDefaultTicket_TicketPresentAndNotDummy() {
      // Given
      String ticketNr2TestConnection = "Ticket-5897";
      Ticket ticket = mockTicket();
      JiraApiReader jiraApiReader = mockJiraApiReader(ticketNr2TestConnection, Optional.of(ticket));
      JiraUserCredentialsAuthenticatorImpl jiraUserCredentialsAuthenticatorImpl =
            new JiraUserCredentialsAuthenticatorImpl(jiraApiReader, ticketNr2TestConnection);

      // When
      boolean actualIsUserAuthentication = jiraUserCredentialsAuthenticatorImpl.doUserAuthentication(USERNAME, "sdf".toCharArray());

      // Then
      assertThat(actualIsUserAuthentication, is(true));
   }


   @Test
   void testDoUserAuthentication_FetchWithDefaultTicket_TicketNotPresent() {
      // Given
      String ticketNr2TestConnection = "Ticket-1234";
      JiraApiReader jiraApiReader = mockJiraApiReader(ticketNr2TestConnection, Optional.empty());
      JiraUserCredentialsAuthenticatorImpl jiraUserCredentialsAuthenticatorImpl =
            new JiraUserCredentialsAuthenticatorImpl(jiraApiReader, ticketNr2TestConnection);

      // When
      boolean actualIsUserAuthentication = jiraUserCredentialsAuthenticatorImpl.doUserAuthentication(USERNAME, "sdf".toCharArray());

      // Then
      assertThat(actualIsUserAuthentication, is(false));
   }

   private static JiraApiReader mockJiraApiReader(String ticketNr2TestConnection, Optional<Ticket> ticketOpt) {
      JiraApiReader jiraApiReader = mock(JiraApiReader.class);
      when(jiraApiReader.readTicket4Nr(eq(ticketNr2TestConnection))).thenReturn(ticketOpt);
      return jiraApiReader;
   }

   private static Ticket mockTicket() {
      Ticket ticket = mock(Ticket.class);
      when(ticket.isDummyTicket()).thenReturn(false);
      return ticket;
   }
}
