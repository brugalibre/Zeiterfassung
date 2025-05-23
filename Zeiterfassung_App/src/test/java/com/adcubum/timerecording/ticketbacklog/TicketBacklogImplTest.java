package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import com.adcubum.timerecording.ticketbacklog.config.TicketConfiguration;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.adcubum.timerecording.settings.common.Const.TICKET_SYSTEM_PROPERTIES;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TicketBacklogImplTest extends BaseTestWithSettings {

   @Test
   void testGetTicketConfiguration() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .buildTestCaseBuilder();

      // When
      TicketConfiguration ticketConfiguration = tcb.ticketBacklog.getTicketConfiguration();

      // Then
      // assuming we have a TicketBacklogImpl here!
      assertThat(ticketConfiguration.getIsDescriptionRequired(), is(false));
      assertThat(ticketConfiguration.getDefaultTicketName(), is("SYRIUS"));
      assertThat(ticketConfiguration.getTicketNamePattern(), is("(([a-zA-Z0-9-?]+)[-][0-9]+)"));
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
   void testGetTicket4Nr_ExistingTicket() throws InterruptedException {
      // Given
      String defaultTicketNr = "INTA-147";
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withRetrievedTicket(defaultTicketNr)
            .buildTestCaseBuilder();

      // When
      tcb.ticketBacklog.initTicketBacklog();
      Thread.sleep(400);

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
      int expectedSize = 1; // since we have one default Ticket configured in the 'defaulTickets.txt'
      TicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withCallbackHandler(callbackHandler)
            .build();

      // When
      ticketBacklog.initTicketBacklog();

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.NOT_CONFIGURED));
      assertThat(ticketBacklog.getTickets().size(), is(expectedSize));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndSuccessfull() throws InterruptedException {
      // Given
      TicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = 2; // since we have one default Ticket configured in the 'defaulTickets.txt'

      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withCallbackHandler(callbackHandler)
            .withBoardName("blubbl")
            .withOneReadTicket()
            .withRetrievedTicket("SYRIUS-984")
            .withSuccessfullRead()
            .build();

      // When
      ticketBacklog.initTicketBacklog();
      Thread.sleep(50);

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.SUCCESS));
      assertThat(ticketBacklog.getTickets().size(), is(expectedSize));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndPartialSuccessful() throws InterruptedException {
      // Given
      TicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = 2; // since we have one default Ticket configured in the 'defaulTickets.txt'

      TicketBacklog ticketBacklog = new TestCaseBuilder()
            .withCallbackHandler(callbackHandler)
            .withBoardName("blubbl")
            .withOneReadTicket()
            .withRetrievedTicket("SYRIUS-984")
            .build();

      // When
      ticketBacklog.initTicketBacklog();
      Thread.sleep(50);

      // Then
      verify(callbackHandler).onTicketBacklogUpdated(eq(UpdateStatus.PARTIAL_SUCCESS));
      assertThat(ticketBacklog.getTickets().size(), is(expectedSize));
   }

   @Test
   void testInitTicketBacklog_ConfiguredAndFail() throws InterruptedException {
      // Given
      TicketBacklogCallbackHandler callbackHandler = spy(new TestUiTicketBacklogCallbackHandler());
      int expectedSize = 1; // since we have one default Ticket configured in the 'defaultTickets.txt'
      TicketBacklog ticketBacklog = new TestCaseBuilder()
              .withCallbackHandler(callbackHandler)
            .withRetrievedTicket("SYRIUS-6354")
            .withBoardName("blubbl")
            .build();

      // When
      ticketBacklog.initTicketBacklog();
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
      private TicketBacklogCallbackHandler callbackHandler;

      private TestCaseBuilder() {
         this.jiraApiReader = mock(JiraApiReader.class);
         this.readTickets = new ArrayList<>();
         this.boardName = "";
         this.callbackHandler = updateStatus -> {
         };
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
         doReturn(Optional.empty()).when(jiraApiReader).readTicket4Nr(eq(null));
         if (nonNull(receivedTicketNr)) {
            Ticket receivedTicket = mockTicket(receivedTicketNr);
            doReturn(Optional.of(receivedTicket)).when(jiraApiReader).readTicket4Nr(eq(receivedTicketNr));
         }
         mockReadTicketsFromBoard();
         saveProperty2Settings("boardName", boardName, TICKET_SYSTEM_PROPERTIES);
         TicketBacklog ticketBacklog = new TicketBacklogImpl(jiraApiReader, createDefaultFileReader(), mock(ServiceCodeAdapter.class));
         ticketBacklog.addTicketBacklogCallbackHandler(callbackHandler);
         return ticketBacklog;
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
         when(jiraApiReader.readTicketsFromBoardAndSprints(any(), any())).thenReturn(JiraApiReadTicketsResult.of(readTickets, isReadBordSuccessfull));
      }

      public TestCaseBuilder withCallbackHandler(TicketBacklogCallbackHandler callbackHandler) {
         this.callbackHandler = callbackHandler;
         return this;
      }
   }

   private static class TestUiTicketBacklogCallbackHandler implements TicketBacklogCallbackHandler {
      @Override
      public void onTicketBacklogUpdated(UpdateStatus updateStatus) {
         // empty
      }
   }
}
