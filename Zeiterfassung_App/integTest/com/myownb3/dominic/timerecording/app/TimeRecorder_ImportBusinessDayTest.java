package com.myownb3.dominic.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.charge.exception.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.core.importexport.in.file.exception.FileImportException;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.myownb3.dominic.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.myownb3.dominic.timerecording.test.BaseTestWithSettings;

class TimeRecorder_ImportBusinessDayTest extends BaseTestWithSettings {

   @Test
   void testImportBusinessDayFromFile_Success() throws InvalidChargeTypeRepresentationException {
      // Given
      File testImportFile = new File("integTestRes\\io\\testImportFile.csv");
      String firstTicketNr = "SYRIUS-1234";
      String firstTicketLeistungsartRep = "113 - Umsetzung/Dokumentation";
      float firstTicketDuration = 1.0f;
      String secondTicketNr = "INTA-556";
      String secondTicketLeistungsartRep = "122 - Qualtitätssicherung";
      float secondTicketDuration = 4.25f;

      // When
      boolean actualImported = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(true));
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().size(), is(2));
      BusinessDayIncrementVO firstBusinessDayInc4TicketNr = findBusinessDayInc4TicketNr(firstTicketNr, bussinessDayVO);
      BusinessDayIncrementVO secondBusinessDayInc4TicketNr = findBusinessDayInc4TicketNr(secondTicketNr, bussinessDayVO);
      assertThat(firstBusinessDayInc4TicketNr, is(notNullValue()));
      assertThat(firstBusinessDayInc4TicketNr.getDescription(), is("Test"));
      assertThat(firstBusinessDayInc4TicketNr.getChargeType(), is(ChargeType.getLeistungsartForRep(firstTicketLeistungsartRep)));
      assertThat(firstBusinessDayInc4TicketNr.getCurrentTimeSnippet().getDuration(), is(firstTicketDuration));
      assertThat(secondBusinessDayInc4TicketNr, is(notNullValue()));
      assertThat(secondBusinessDayInc4TicketNr.getDescription(), is(""));
      assertThat(secondBusinessDayInc4TicketNr.getChargeType(), is(ChargeType.getLeistungsartForRep(secondTicketLeistungsartRep)));
      assertThat(secondBusinessDayInc4TicketNr.getCurrentTimeSnippet().getDuration(), is(secondTicketDuration));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_InvalidChargeType() {
      // Given
      File testImportFile = new File("integTestRes\\io\\testImportFileWithInvalidChargeType.csv");

      // When
      boolean actualImported = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_WithMultipleBeginAndEndElements() {
      // Given
      File testImportFile = new File("integTestRes\\io\\testImportFileWithMultipleBeginEnd.csv");

      // When
      boolean actualImported = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_WithoutHeaderOrWrongHeader() {
      // Given
      File testImportFile = new File("integTestRes\\io\\testImportFileWithoutHeader.csv");
      File testImportFile2 = new File("integTestRes\\io\\testImportFileWithEmptyHeader.csv");

      // When
      boolean actualImported = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);
      boolean actualImported2 = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile2);

      // Then
      assertThat(actualImported, is(false));
      assertThat(actualImported2, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_EmptyFile() {
      // Given
      File testImportFile = new File("integTestRes\\io\\emptyTestImport.csv");

      // When
      boolean actualImported = TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);

      // Then
      assertThat(actualImported, is(false));
   }

   @Test
   void testImportBusinessDayFromFile_Failure_FileDoesNotExist() {
      // Given
      File testImportFile = new File("integTestRes\\io\\IDontEvenExist.csv");

      // When
      Executable ex = () -> TimeRecorder.INSTANCE.importBusinessDayFromFile(testImportFile);

      // Then
      assertThrows(FileImportException.class, ex);
   }

   private BusinessDayIncrementVO findBusinessDayInc4TicketNr(String firstTicketNr, BusinessDayVO bussinessDayVO) {
      return bussinessDayVO.getBusinessDayIncrements()
            .stream()
            .filter(bdIncrement -> bdIncrement.getTicketNumber().equals(firstTicketNr))
            .findFirst()
            .orElse(null);
   }
}