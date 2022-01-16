package com.adcubum.timerecording.app;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.coolguys.exception.InvalidChargeTypeRepresentationException;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryIntegMockUtil;
import com.adcubum.timerecording.importexport.in.file.exception.FileImportException;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TimeRecorder_ImportBusinessDayTest {

   @Test
   void testImportBusinessDayFromFile_Success() throws InvalidChargeTypeRepresentationException {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\testImportFile.csv");
      String ticketNr = "SYRIUS-1234";
      String firstTicketLeistungsartRep = "113 - Umsetzung/Dokumentation";
      float firstTicketDuration = 1.0f;
      String secondTicketNr = "INTA-556";
      String secondTicketLeistungsartRep = "122 - Qualtitätssicherung";
      int serviceCodeTesting = 122;
      float secondTicketDuration = 4.25f;
      TimeRecorder timeRecorder = buildTimeRecorder();
      TicketBacklogSPI.getTicketBacklog().initTicketBacklog();

      // When
      boolean actualImported = timeRecorder.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(true));
      BusinessDay bussinessDay = timeRecorder.getBussinessDay();
      assertThat(bussinessDay.getIncrements().size(), is(2));
      BusinessDayIncrement firstBusinessDayInc4TicketNr = findBusinessDayInc4TicketNr(ticketNr, bussinessDay);
      BusinessDayIncrement secondBusinessDayInc4TicketNr = findBusinessDayInc4TicketNr(secondTicketNr, bussinessDay);
      assertThat(firstBusinessDayInc4TicketNr, is(notNullValue()));
      assertThat(firstBusinessDayInc4TicketNr.getDescription(), is("Test"));
      assertThat(firstBusinessDayInc4TicketNr.getTicketActivity().getActivityCode(), is(113));
      assertThat(firstBusinessDayInc4TicketNr.getTicketActivity().getActivityName(), is(firstTicketLeistungsartRep));
      assertThat(firstBusinessDayInc4TicketNr.getCurrentTimeSnippet().getDuration(), is(firstTicketDuration));
      assertThat(secondBusinessDayInc4TicketNr, is(notNullValue()));
      assertThat(secondBusinessDayInc4TicketNr.getDescription(), is(""));
      assertThat(secondBusinessDayInc4TicketNr.getTicketActivity().getActivityCode(), is(serviceCodeTesting));
      assertThat(secondBusinessDayInc4TicketNr.getTicketActivity().getActivityName(), is(secondTicketLeistungsartRep));
      assertThat(secondBusinessDayInc4TicketNr.getCurrentTimeSnippet().getDuration(), is(secondTicketDuration));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_InvalidChargeType() {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\testImportFileWithInvalidChargeType.csv");
      TimeRecorder timeRecorder = buildTimeRecorder();

      // When
      boolean actualImported = timeRecorder.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_WithMultipleBeginAndEndElements() {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\testImportFileWithMultipleBeginEnd.csv");
      TimeRecorder timeRecorder = buildTimeRecorder();

      // When
      boolean actualImported = timeRecorder.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_WithoutHeaderOrWrongHeader() {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\testImportFileWithoutHeader.csv");
      File testImportFile2 = new File("src\\integTest\\resources\\io\\testImportFileWithEmptyHeader.csv");
      TimeRecorder timeRecorder = buildTimeRecorder();

      // When
      boolean actualImported = timeRecorder.importBusinessDayFromFile(testImportFile);
      boolean actualImported2 = timeRecorder.importBusinessDayFromFile(testImportFile2);

      // Then
      assertThat(actualImported, is(false));
      assertThat(actualImported2, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_EmptyFile() {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\emptyTestImport.csv");
      TimeRecorder timeRecorder = buildTimeRecorder();

      // When
      boolean actualImported = timeRecorder.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_FileDoesNotExist() {
      // Given
      File testImportFile = new File("src\\integTest\\resources\\io\\IDontEvenExist.csv");
      TimeRecorder timeRecorder = buildTimeRecorder();

      // When
      Executable ex = () -> timeRecorder.importBusinessDayFromFile(testImportFile);

      // Then
      assertThrows(FileImportException.class, ex);
   }

   private BusinessDayIncrement findBusinessDayInc4TicketNr(String firstTicketNr, BusinessDay bussinessDay) {
      return bussinessDay.getIncrements()
            .stream()
            .filter(bdIncrement -> bdIncrement.getTicket().getNr().equals(firstTicketNr))
            .findFirst()
            .orElse(null);
   }

   private static TimeRecorder buildTimeRecorder() {
      TimeRecorder timeRecorder =
            new TimeRecorderImpl(mock(BookerAdapter.class), BusinessDayRepositoryIntegMockUtil.mockBusinessDayRepository(new BusinessDayImpl()));
      timeRecorder.init();
      return timeRecorder;
   }
}
