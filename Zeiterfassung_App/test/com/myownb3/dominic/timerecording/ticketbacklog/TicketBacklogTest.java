package com.myownb3.dominic.timerecording.ticketbacklog;

import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.myownb3.dominic.timerecording.test.BaseTestWithSettings;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.defaulttickets.DefaultTicketConst;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

@RunWith(JUnitPlatform.class)
class TicketBacklogTest extends BaseTestWithSettings {

   private static final int AMOUNT_OF_DEFAULT_TICKETS = DefaultTicketConst.getDefaultScrumtTicketNrs().size();

   @Test
   void testInitTicketBacklog() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .buildTestCaseBuilder();
      // When
      tcb.ticketBacklog.initTicketBacklog(res -> {
      });

      // Then
      verify(tcb.jiraApiReader, times(AMOUNT_OF_DEFAULT_TICKETS)).readTicket4Nr(any());
   }

   @Test
   void testInitTicketBacklog_NotConfigured() {
      // Given
      UiTicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .build();

      // When
      ticketBacklog.initTicketBacklog(callbackHandler);

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.NOT_CONFIGURED));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndSuccessfull() throws InterruptedException {
      // Given
      UiTicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = 1 + AMOUNT_OF_DEFAULT_TICKETS;// 1 plus 4 default tickets
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withBoardName("blubbl")
            .withOneReadTicket()
            .withSuccessfullRead()
            .build();

      // When
      ticketBacklog.initTicketBacklog(callbackHandler);
      Thread.sleep(50);

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.SUCCESS));
      assertThat(ticketBacklog.getTickets().size(), is(expectedSize));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndFail() throws InterruptedException {
      // Given
      UiTicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = AMOUNT_OF_DEFAULT_TICKETS;// Only the default tickets
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withBoardName("blubbl")
            .build();

      // When
      ticketBacklog.initTicketBacklog(callbackHandler);
      Thread.sleep(50);

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.FAIL));
      assertThat(ticketBacklog.getTickets().size(), is(expectedSize));
   }

   private static final class TestCaseBuilder {

      private JiraApiReader jiraApiReader;
      private boolean isReadBordSuccessfull;
      private String boardName;
      private List<Ticket> readTickets;
      private String defaultTicketNr;
      private TicketBacklog ticketBacklog;

      private TestCaseBuilder() {
         this.jiraApiReader = mock(JiraApiReader.class);
         this.readTickets = new ArrayList<>();
         this.defaultTicketNr = "SYRIUS-1324";
      }

      public TestCaseBuilder withOneReadTicket() {
         readTickets.add(mock(Ticket.class));
         return this;
      }

      private TestCaseBuilder withSuccessfullRead() {
         this.isReadBordSuccessfull = true;
         return this;
      }

      private TestCaseBuilder withBoardName(String boardName) {
         this.boardName = boardName;
         return this;
      }

      private TicketBacklog build() {
         Ticket defaultTicket = mock(Ticket.class);
         when(defaultTicket.getNr()).thenReturn(defaultTicketNr);
         when(jiraApiReader.readTicket4Nr(any())).thenReturn(Optional.of(defaultTicket));
         mockReadTicketsFromBoard();
         if (nonNull(boardName)) {
            saveProperty2Settings("boardName", boardName);
         }
         return new TicketBacklog(jiraApiReader);
      }

      private TestCaseBuilder buildTestCaseBuilder() {
         this.ticketBacklog = build();
         return this;
      }

      private void mockReadTicketsFromBoard() {
         Exception ex = null;
         if (!isReadBordSuccessfull) {
            ex = new RuntimeException();
         }
         when(jiraApiReader.readTicketsFromBoard(any())).thenReturn(JiraApiReadTicketsResult.of(readTickets, ex));
      }
   }

   private static class TestUiTicketBacklogCallbackHandler implements UiTicketBacklogCallbackHandler {
      @Override
      public void onTicketBacklogUpdated(UpdateStatus updateStatus) {
         // empty
      }
   }
}
