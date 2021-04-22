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
import com.adcubum.timerecording.work.date.Time;

class ComeAndGoImplTest extends BaseTestWithSettings {

   @Test
   void testComeOrGoWhenItsAlreadyDone() {
      // Given

      // When
      Executable exe = () -> {
         ComeAndGo comeAndGo = ComeAndGoImpl.of();
         comeAndGo = comeAndGo.comeOrGo(new Time());
         comeAndGo = comeAndGo.comeOrGo(new Time());// this comeAndGo is done by now
         comeAndGo.comeOrGo(new Time());
      };

      // Then
      assertThrows(IllegalStateException.class, exe);
   }

   @Test
   void testRepresentationOfADoneComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / 00:11";

      Time beginTimeStamp = getTime(10);
      Time endTimeStamp = getTime(11);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      comeAndGo = comeAndGo.comeOrGo(endTimeStamp);
      String actualRepresentation = comeAndGo.getRepresentation();

      // Then
      assertThat(actualRepresentation, is(expectedRepresentation));
   }

   @Test
   void testRepresentationOfAUnfinishedComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / -";

      Time beginTimeStamp = getTime(10);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      String actualRepresentation = comeAndGo.getRepresentation();

      // Then
      assertThat(actualRepresentation, is(expectedRepresentation));
   }

   @Test
   void testRepresentationOfAFinishedRecordedComeOrGo() {
      // Given
      ComeAndGo comeAndGo = ComeAndGoImpl.of();
      String expectedRepresentation = "00:10 / 00:11 " + TextLabel.COME_OR_GO_RECORDED_TEXT;

      Time beginTimeStamp = getTime(10);
      Time endTimeStamp = getTime(11);

      // When
      comeAndGo = comeAndGo.comeOrGo(beginTimeStamp);
      comeAndGo = comeAndGo.comeOrGo(endTimeStamp);
      comeAndGo = comeAndGo.flagAsRecorded();
      String actualRepresentation = comeAndGo.getRepresentation();

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

   private static Time getTime(int amountOfMinutes) {
      GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
      int oneMinute = 1 * 60000;
      return new Time(startDate.getTimeInMillis() + amountOfMinutes * oneMinute);
   }
}
