package com.adcubum.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.importexport.out.file.FileExportResult;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.util.parser.DateParser;

class TimeRecorder_ExportBusinessDayTest {

   @BeforeEach
   public void tearDown() {
      try {
         // make sure there is no garbage..
         TimeRecorder.INSTANCE.clear();
      } catch (Exception e) {
         e.printStackTrace();
         // silently ignore -.-
      }
   }

   @Test
   void testExpotBusinessDayFromFile_Success() throws IOException, ParseException {
      // Given
      File expectedExportedFile = new File("src\\integTest\\resources\\io\\expectedExportedBusinessDay.csv");
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
      FileImporter fileImporter = FileImporterFactory.createNew();
      List<String> actualFileContent = fileImporter.importFile(exportedFile);
      List<String> expectedContent = fileImporter.importFile(expectedExportedFile);
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

      private TestCaseBuilder withBusinessDayIncrement(String ticketNr, String description, int kindOfService, int timeSnippedDuration)
            throws ParseException {
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

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) throws ParseException {
         Date startDate = DateParser.parse2Date("01-02-2020 00:00", DateParser.DATE_PATTERN);
         Time beginTimeStamp = TimeFactory.createNew(startDate.getTime());
         TimeSnippet timeSnippet = TimeSnippetFactory.createNew();
         timeSnippet.setBeginTimeStamp(beginTimeStamp);
         timeSnippet.setEndTimeStamp(TimeFactory.createNew(startDate.getTime() + timeBetweenBeginAndEnd));
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

      @Override
      public void onCome() {}

      @Override
      public void onGo() {}
   }
}
