package com.adcubum.timerecording.core.work.businessday.comeandgo.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

class ComeAndGoImplTest extends BaseTestWithSettings {

   @Test
   void testComeOrGoWhenItsAlreadyDone() {
      // Given

      // When
      Executable exe = () -> {
         ComeAndGo comeAndGo = ComeAndGoImpl.of();
         comeAndGo = comeAndGo.comeOrGo(DateTimeFactory.createNew());
         comeAndGo = comeAndGo.comeOrGo(DateTimeFactory.createNew());// this comeAndGo is done by now
         comeAndGo.comeOrGo(DateTimeFactory.createNew());
      };

      // Then
      assertThrows(IllegalStateException.class, exe);
   }

   @Test
   void testRepresentationOfADoneComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / 00:11";

      DateTime beginTimeStamp = getTime(10);
      DateTime endTimeStamp = getTime(11);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      comeAndGo = comeAndGo.comeOrGo(endTimeStamp);
      String actualRepresentation = comeAndGo.toString();

      // Then
      assertThat(actualRepresentation, is(expectedRepresentation));
   }

   @Test
   void testRepresentationOfAUnfinishedComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / -";

      DateTime beginTimeStamp = getTime(10);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      String actualRepresentation = comeAndGo.toString();

      // Then
      assertThat(actualRepresentation, is(expectedRepresentation));
   }

   @Test
   void testRepresentationOfAFinishedRecordedComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / 00:11 " + TextLabel.EXISTS_RECORD_FOR_COME_AND_GO;

      DateTime beginTimeStamp = getTime(10);
      DateTime endTimeStamp = getTime(11);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      comeAndGo = comeAndGo.comeOrGo(endTimeStamp);
      comeAndGo = comeAndGo.flagAsRecorded();
      String actualRepresentation = comeAndGo.toString();

      // Then
      assertThat(actualRepresentation, is(expectedRepresentation));
   }

   @Test
   void testNewComeOrGoIsNotDoneNorRecorded() {
      // Given
      // When
      ComeAndGoImpl comeAndGo = ComeAndGoImpl.of();

      // Then
      assertThat(comeAndGo.isNotDone(), is(true));
      assertThat(comeAndGo.isNotRecorded(), is(true));
   }

   private static DateTime getTime(int amountOfMinutes) {
      GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
      int oneMinute = 1 * 60000;
      return DateTimeFactory.createNew(startDate.getTimeInMillis() + amountOfMinutes * oneMinute);
   }
}
