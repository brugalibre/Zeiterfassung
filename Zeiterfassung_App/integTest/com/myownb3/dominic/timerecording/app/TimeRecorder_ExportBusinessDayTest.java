package com.myownb3.dominic.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.dominic.timerecording.core.callbackhandler.UiCallbackHandler;
import com.myownb3.dominic.timerecording.core.importexport.in.file.FileImporter;
import com.myownb3.dominic.timerecording.core.importexport.out.file.FileExportResult;
import com.myownb3.dominic.timerecording.core.message.Message;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;

class TimeRecorder_ExportBusinessDayTest {

   @Test
   void testExpotBusinessDayFromFile_Success() throws IOException {
      // Given
      File expectedExportedFile = new File("integTestRes\\io\\expectedExportedBusinessDay.csv");
      String ticketNr = "SYRIUS-1234";
      String description = "Test";
      int kindOfService = 113;
      int timeSnippedDuration = 3600 * 1000;
      TestUiCallbackHandler uiCallbackHandler = spy(new TestUiCallbackHandler());
      new TestCaseBuilder()
            .withBusinessDayIncrement(ticketNr, description, kindOfService, timeSnippedDuration)
            .withUiCallbackHandler(uiCallbackHandler)
            .build();

      // When
      FileExportResult fileExportResult = TimeRecorder.INSTANCE.export();

      // Then
      verify(uiCallbackHandler).displayMessage(any());
      assertThat(fileExportResult.isSuccess(), is(true));
      File exportedFile = assertFileContent(expectedExportedFile, fileExportResult);
      Files.delete(exportedFile.toPath());
   }

   private File assertFileContent(File expectedExportedFile, FileExportResult fileExportResult) {
      File exportedFile = new File(fileExportResult.getPath());
      assertThat(exportedFile.exists(), is(true));
      List<String> actualFileContent = FileImporter.INTANCE.importFile(exportedFile);
      List<String> expectedContent = FileImporter.INTANCE.importFile(expectedExportedFile);
      assertContent(expectedContent, actualFileContent);
      return exportedFile;
   }

   private static void assertContent(List<String> expectedContent, List<String> importedContent) {
      for (int i = 0; i < importedContent.size(); i++) {
         String importedLine = importedContent.get(i);
         assertThat(importedLine, is(expectedContent.get(i)));
      }
   }

   private static class TestCaseBuilder {

      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
      }

      private TestCaseBuilder withUiCallbackHandler(UiCallbackHandler callbackHandler) {
         TimeRecorder.INSTANCE.setCallbackHandler(callbackHandler);
         return this;
      }

      private TestCaseBuilder withBusinessDayIncrement(String ticketNr, String description, int kindOfService, int timeSnippedDuration) {
         Ticket ticket = mockTicket(ticketNr);
         businessDayIncrementAdds.add(new BusinessDayIncrementAddBuilder()
               .withTimeSnippet(createTimeSnippet(timeSnippedDuration))
               .withDescription(description)
               .withTicket(ticket)
               .withKindOfService(kindOfService)
               .build());
         return this;
      }

      private Ticket mockTicket(String ticketNr) {
         Ticket ticket = mock(Ticket.class);
         when(ticket.getNr()).thenReturn(ticketNr);
         return ticket;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            TimeRecorder.INSTANCE.addBusinessIncrement(businessDayIncrementAdd);
         }
      }

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
         GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
         Time beginTimeStamp = new Time(startDate.getTimeInMillis());
         TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
         timeSnippet.setBeginTimeStamp(beginTimeStamp);
         timeSnippet.setEndTimeStamp(new Time(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
         return timeSnippet;
      }
   }

   private static class TestUiCallbackHandler implements UiCallbackHandler {

      @Override
      public void onStop() {

      }

      @Override
      public void onStart() {}

      @Override
      public void onResume() {}

      @Override
      public void onException(Throwable throwable, Thread t) {}

      @Override
      public void displayMessage(Message message) {

      }
   }
}
