package com.adcubum.timerecording.app;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryIntegMockUtil;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.importexport.out.file.FileExportResult;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.messaging.send.BookBusinessDayMessageSender;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.util.parser.DateParser;
import org.junit.jupiter.api.Test;

import javax.swing.text.TabableView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TimeRecorder_ExportBusinessDayTest {

   @Test
   void testExpotBusinessDayFromFile_Success() throws IOException, ParseException {
      // Given
      File expectedExportedFile = new File("src\\integTest\\resources\\io\\expectedExportedBusinessDay.csv");
      String ticketNr = "SYRIUS-1234";
      String description = "Test";
      int serviceCode = 113;
      TicketActivity ticketActivity = TicketActivityFactory.INSTANCE.createNew("Umsetzung/Dokumentation", serviceCode);
      int timeSnippedDuration = 3600 * 1000;
      TestUiCallbackHandler uiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withBusinessDayIncrement(ticketNr, description, ticketActivity, timeSnippedDuration)
            .withUiCallbackHandler(uiCallbackHandler)
            .build();

      // When
      FileExportResult fileExportResult = tcb.timeRecorder.export();

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
      private TimeRecorderImpl timeRecorder;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
         BusinessDayRepository businessDayRepository = BusinessDayRepositoryIntegMockUtil.mockBusinessDayRepository(new BusinessDayImpl());
         this.timeRecorder = new TimeRecorderImpl(mock(BookerAdapter.class), businessDayRepository, mock(BookBusinessDayMessageSender.class));
         this.timeRecorder.init();
      }

      private TestCaseBuilder withUiCallbackHandler(UiCallbackHandler callbackHandler) {
         timeRecorder.setCallbackHandler(callbackHandler);
         return this;
      }

      private TestCaseBuilder withBusinessDayIncrement(String ticketNr, String description, TicketActivity ticketActivity, int timeSnippedDuration)
            throws ParseException {
         Ticket ticket = mockTicket(ticketNr);
         businessDayIncrementAdds.add(new BusinessDayIncrementAddBuilder()
               .withTimeSnippet(createTimeSnippet(timeSnippedDuration))
               .withDescription(description)
               .withTicket(ticket)
               .withTicketActivity(ticketActivity)
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
            timeRecorder.addBusinessIncrement(businessDayIncrementAdd);
         }
      }

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) throws ParseException {
         Date startDate = DateParser.parse2Date("01.01.2020 00:00", DateParser.DATE_PATTERN);
         DateTime beginTimeStamp = DateTimeFactory.createNew(startDate.getTime());
         return TimeSnippetBuilder.of()
               .withBeginTime(beginTimeStamp)
               .withEndTime(DateTimeFactory.createNew(startDate.getTime() + timeBetweenBeginAndEnd))
               .build();
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
