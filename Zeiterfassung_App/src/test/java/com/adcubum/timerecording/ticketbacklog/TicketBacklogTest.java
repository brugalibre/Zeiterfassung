package com.adcubum.timerecording.ticketbacklog;

import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.defaulttickets.DefaultTicketConst;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;

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
   void testGetTicket4Nr_NewTicket() {
      // Given
      String ticketNr = "165416574";
      int expectedAmountOfTicketsAfter = 1;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withRetrievedTicket(ticketNr)
            .buildTestCaseBuilder();

      // When
      Ticket actualTicket = tcb.ticketBacklog.getTicket4Nr(ticketNr);

      // Then
      int actualAmountOfTicketsAfter = tcb.ticketBacklog.getTickets().size();
      verify(tcb.jiraApiReader).readTicket4Nr(eq(ticketNr));
      assertThat(actualTicket, is(notNullValue()));
      assertThat(actualAmountOfTicketsAfter, is(expectedAmountOfTicketsAfter));
   }

   @Test
   void testGetTicket4Nr_NrIsNull() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .buildTestCaseBuilder();

      // When
      int amountOfTicketsBefore = tcb.ticketBacklog.getTickets().size();
      Ticket actualTicket = tcb.ticketBacklog.getTicket4Nr(null);

      // Then
      int amountOfTicketsAfter = tcb.ticketBacklog.getTickets().size();
      verify(tcb.jiraApiReader).readTicket4Nr(eq(null));
      assertThat(actualTicket, is(notNullValue()));
      assertThat(actualTicket.getNr(), is(nullValue()));
      assertThat(amountOfTicketsBefore, is(amountOfTicketsAfter));
   }

   @Test
   void testGetTicket4Nr_ExistingTicket() {
      // Given
      String defaultTicketNr = "INTA-147";
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withRetrievedTicket(defaultTicketNr)
            .buildTestCaseBuilder();

      // When
      tcb.ticketBacklog.initTicketBacklog(res -> {
      });
      int amountOfTicketsBefore = tcb.ticketBacklog.getTickets().size();
      Ticket actualTicket = tcb.ticketBacklog.getTicket4Nr(defaultTicketNr);// this must not trigger the jiraApi

      // Then
      int amountOfTicketsAfter = tcb.ticketBacklog.getTickets().size();
      verify(tcb.jiraApiReader).readTicket4Nr(eq(defaultTicketNr));// call only once, while initializing the default tickets
      assertThat(actualTicket, is(notNullValue()));
      assertThat(amountOfTicketsBefore, is(amountOfTicketsAfter));
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
      assertThat(ticketBacklog.getTickets().size(), is(AMOUNT_OF_DEFAULT_TICKETS));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndSuccessfull() throws InterruptedException {
      // Given
      UiTicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = 1 + AMOUNT_OF_DEFAULT_TICKETS;// 1 plus 4 default tickets
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withBoardName("blubbl")
            .withOneReadTicket()
            .withRetrievedTicket("SYRIUS-984")
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
      int expectedSize = AMOUNT_OF_DEFAULT_TICKETS;
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withRetrievedTicket("SYRIUS-6354")
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
      private TicketBacklog ticketBacklog;
      private String receivedTicketNr;

      private TestCaseBuilder() {
         this.jiraApiReader = mock(JiraApiReader.class);
         this.readTickets = new ArrayList<>();
      }

      public TestCaseBuilder withOneReadTicket() {
         readTickets.add(mock(Ticket.class));
         return this;
      }

      private TestCaseBuilder withSuccessfullRead() {
         this.isReadBordSuccessfull = true;
         return this;
      }

      private TestCaseBuilder withRetrievedTicket(String receivedTicketNr) {
         this.receivedTicketNr = receivedTicketNr;
         return this;
      }

      private Ticket mockTicket(String ticketNr) {
         Ticket newTicket = mock(Ticket.class);
         when(newTicket.getNr()).thenReturn(ticketNr);
         when(newTicket.isBookable()).thenReturn(nonNull(ticketNr));
         return newTicket;
      }

      private TestCaseBuilder withBoardName(String boardName) {
         this.boardName = boardName;
         return this;
      }

      private TicketBacklog build() {
         Ticket defaultTicket = mock(Ticket.class);
         when(defaultTicket.getNr()).thenReturn("13254548");
         when(jiraApiReader.readTicket4Nr(any())).thenReturn(Optional.of(defaultTicket));
         for (String ticketNr : DefaultTicketConst.getDefaultScrumtTicketNrs()) {
            Ticket ticket = mockTicket(ticketNr);
            doReturn(Optional.of(ticket)).when(jiraApiReader).readTicket4Nr(eq(ticketNr));
         }
         doReturn(Optional.empty()).when(jiraApiReader).readTicket4Nr(eq(null));
         if (nonNull(receivedTicketNr)) {
            Ticket receivedTicket = mockTicket(receivedTicketNr);
            doReturn(Optional.of(receivedTicket)).when(jiraApiReader).readTicket4Nr(eq(receivedTicketNr));
         }
         mockReadTicketsFromBoard();
         if (nonNull(boardName)) {
            saveProperty2Settings("boardName", boardName);
         }
         return new TicketBacklog(jiraApiReader, createDefaultFileReader());
      }

      private FileImporter createDefaultFileReader() {
         return file -> {
            List<String> importedLines = new ArrayList<>();
            try (FileReader fileReader = new FileReader(file)) {
               BufferedReader bufferedReader = new BufferedReader(fileReader);
               String readLine = bufferedReader.readLine();
               while (Objects.nonNull(readLine)) {
                  importedLines.add(readLine);
                  readLine = bufferedReader.readLine();
               }
               return importedLines;
            } catch (IOException e) {
               e.printStackTrace();
            }
            return importedLines;
         };
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
         when(jiraApiReader.readTicketsFromBoardAndSprints(any(), any())).thenReturn(JiraApiReadTicketsResult.of(readTickets, ex));
      }
   }

   private static class TestUiTicketBacklogCallbackHandler implements UiTicketBacklogCallbackHandler {
      @Override
      public void onTicketBacklogUpdated(UpdateStatus updateStatus) {
         // empty
      }
   }
}
